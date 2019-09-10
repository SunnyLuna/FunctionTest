package com.decard.zj.founctiontest

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import android.view.TextureView
import android.view.WindowManager
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * 自定义camera2
 * @author ZJ
 * created at 2019/5/16 11:13
 */
class ZhaoWeiCamera {


    private var mCameraId = "0"//摄像头id（通常0代表后置摄像头，1代表前置摄像头）
    private lateinit var mCharacteristics: CameraCharacteristics
    private var mCameraDevice: CameraDevice? = null
    private var mCameraCaptureSession: CameraCaptureSession? = null
    private var mPreviewImageReader: ImageReader? = null
    private var mPhotoImageReader: ImageReader? = null

    private var previewSize: Size? = null

    private var textureView: TextureView? = null
    private var mContext: Context? = null
    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private var backgroundThread: HandlerThread? = null

    /**
     * A [Handler] for running tasks in the background.
     */
    private var backgroundHandler: Handler? = null


    private var sensorOrientation = 0
    private val MAX_PREVIEW_WIDTH = 1920
    private val MAX_PREVIEW_HEIGHT = 1080
    private var mdatas: ByteArray? = null

    companion object {

        val instance: ZhaoWeiCamera get() = Holder.cameraUtils

        private val TAG = "----ZhaoWeiCameraUtils"
        private val ORIENTATIONS = SparseIntArray()

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }
    }

    private object Holder {
        val cameraUtils = ZhaoWeiCamera()
    }


    /**
     * 打开摄像头
     */
    fun startCamera(tv: TextureView, context: Context) {
        startBackgroundThread()
        this.mContext = context
        textureView = tv
        if (tv.isAvailable) {
            openCamera(textureView!!.width, textureView!!.height)
        } else {
            tv.surfaceTextureListener = surfaceTextureListener
        }
    }


    /**
     * 打开摄像头
     */
    @SuppressLint("MissingPermission")
    private fun openCamera(width: Int, height: Int) {
        val manager = mContext!!.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        //设置摄像头特性
        setCameraCharacteristics(manager, width, height)
        try {
            manager.openCamera(mCameraId, stateCallback, backgroundHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }


    /**
     * 设置摄像头的参数
     */
    private fun setCameraCharacteristics(manager: CameraManager, width: Int, height: Int) {
        try {
            // 获取指定摄像头的特性
            mCharacteristics = manager.getCameraCharacteristics(mCameraId)
            // 获取摄像头支持的配置属性
            val map = mCharacteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            // 获取摄像头支持的最大尺寸
            val largestPreview = Collections.max(
                    Arrays.asList(*map!!.getOutputSizes(ImageFormat.YUV_420_888)), CompareSizesByArea())
            // 创建一个ImageReader对象，用于获取摄像头的图像数据
            mPreviewImageReader = ImageReader.newInstance(largestPreview.width, largestPreview.height,
                    ImageFormat.YUV_420_888, 1)
            //设置获取图片的监听
            mPreviewImageReader!!.setOnImageAvailableListener(previewAvailableListener, backgroundHandler)

            val largestPhoto = Collections.max(
                    Arrays.asList(*map.getOutputSizes(ImageFormat.JPEG)), CompareSizesByArea())
            // 创建一个ImageReader对象，用于获取摄像头的图像数据
            mPhotoImageReader = ImageReader.newInstance(largestPhoto.width, largestPhoto.height,
                    ImageFormat.JPEG, 2)
            //设置获取图片的监听
            mPhotoImageReader!!.setOnImageAvailableListener(previewAvailableListener, backgroundHandler)

            // Find out if we need to swap dimension to get the preview size relative to sensor
            // coordinate.
            val displayRotation = (mContext!!
                    .getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation

            sensorOrientation = mCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)
            val swappedDimensions = areDimensionsSwapped(displayRotation)

            val displaySize = Point()
            (mContext!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(displaySize)
            val rotatedPreviewWidth = if (swappedDimensions) height else width
            val rotatedPreviewHeight = if (swappedDimensions) width else height
            var maxPreviewWidth = if (swappedDimensions) displaySize.y else displaySize.x
            var maxPreviewHeight = if (swappedDimensions) displaySize.x else displaySize.y

            if (maxPreviewWidth > MAX_PREVIEW_WIDTH) maxPreviewWidth = MAX_PREVIEW_WIDTH
            if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) maxPreviewHeight = MAX_PREVIEW_HEIGHT

            // 获取最佳的预览尺寸
            previewSize = chooseOptimalSize(map.getOutputSizes(
                    SurfaceTexture::class.java),
                    rotatedPreviewWidth, rotatedPreviewHeight,
                    maxPreviewWidth, maxPreviewHeight, largestPhoto)

        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

    }

    /**
     * Determines if the dimensions are swapped given the phone's current rotation.
     *
     * @param displayRotation The current rotation of the display
     *
     * @return true if the dimensions are swapped, false otherwise.
     */
    private fun areDimensionsSwapped(displayRotation: Int): Boolean {
        var swappedDimensions = false
        when (displayRotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 -> {
                if (sensorOrientation == 90 || sensorOrientation == 270) {
                    swappedDimensions = true
                }
            }
            Surface.ROTATION_90, Surface.ROTATION_270 -> {
                if (sensorOrientation == 0 || sensorOrientation == 180) {
                    swappedDimensions = true
                }
            }
            else -> {
                Log.e(TAG, "Display rotation is invalid: $displayRotation")
            }
        }
        return swappedDimensions
    }

    /**
     * 选择最佳的预览尺寸
     *
     * @param choices
     * @param width
     * @param height
     * @param aspectRatio
     * @return
     */
    private fun chooseOptimalSize(
            choices: Array<Size>,
            textureViewWidth: Int,
            textureViewHeight: Int,
            maxWidth: Int,
            maxHeight: Int,
            aspectRatio: Size
    ): Size {

        // Collect the supported resolutions that are at least as big as the preview Surface
        val bigEnough = ArrayList<Size>()
        // Collect the supported resolutions that are smaller than the preview Surface
        val notBigEnough = ArrayList<Size>()
        val w = aspectRatio.width
        val h = aspectRatio.height
        for (option in choices) {
            if (option.width <= maxWidth && option.height <= maxHeight &&
                    option.height == option.width * h / w) {
                if (option.width >= textureViewWidth && option.height >= textureViewHeight) {
                    bigEnough.add(option)
                } else {
                    notBigEnough.add(option)
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size > 0) {
            return Collections.min(bigEnough, CompareSizesByArea())
        } else if (notBigEnough.size > 0) {
            return Collections.max(notBigEnough, CompareSizesByArea())
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size")
            return choices[0]
        }
    }

    // 为Size定义一个比较器Comparator
    internal class CompareSizesByArea : Comparator<Size> {
        override fun compare(lhs: Size, rhs: Size): Int {
            // 强转为long保证不会发生溢出
            return java.lang.Long.signum(lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height)
        }
    }

    /**
     * TextureView的监听
     */
    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {

        //可用
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            openCamera(width, height)
        }


        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {

        }

        //释放
        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            closeCamera()
            return true
        }

        //更新
        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

        }
    }


    /**
     * 摄像头状态的监听
     */
    private val stateCallback = object : CameraDevice.StateCallback() {
        // 摄像头被打开时触发该方法
        override fun onOpened(cameraDevice: CameraDevice) {
            mCameraDevice = cameraDevice
            // 开始预览
            takePreview()
        }

        // 摄像头断开连接时触发该方法
        override fun onDisconnected(cameraDevice: CameraDevice) {
            cameraDevice.close()
            mCameraDevice = null
        }

        // 打开摄像头出现错误时触发该方法
        override fun onError(cameraDevice: CameraDevice, error: Int) {
            cameraDevice.close()
        }
    }

    /**
     * 监听预览数据的方法
     */
    private val previewAvailableListener = ImageReader.OnImageAvailableListener { reader ->
        // 当照片数据可用时激发该方法
        // 获取捕获的照片数据
        val image = reader.acquireLatestImage()
        if (image.format == ImageFormat.JPEG) {
            Observable.create(ObservableOnSubscribe<Bitmap> {
                val buffer = image.planes[0].buffer
                val data = ByteArray(buffer.remaining())
                buffer.get(data)
                image.close()
                //显示图片
                val options = BitmapFactory.Options()
                options.inSampleSize = 2

                var bitmap = BitmapFactory.decodeByteArray(data, 0, data.size, options)
                val matrix = Matrix()
                if (mCameraId == "1") {
                    matrix.setScale(-1f, 1f)
                }
                matrix.postRotate(90f)
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                it.onNext(bitmap)
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                Log.d(TAG, Thread.currentThread().name)
            }
        } else {
            val buffer = image.planes[0].buffer
            val data = ByteArray(buffer.remaining())
            buffer.get(data)
            mdatas = data
            Log.d("-----------", mdatas.toString())
            image.close()
        }
    }


    /**
     * 开始预览
     */
    private fun takePreview() {

        Log.d(TAG, "takePreview: " + previewSize!!.height)
        val surfaceTexture = textureView!!.surfaceTexture
        //设置TextureView的缓冲区大小
        surfaceTexture.setDefaultBufferSize(previewSize!!.width, previewSize!!.height)
        //获取Surface显示预览数据
        val mSurface = Surface(surfaceTexture)
        try {
            //创建预览请求
            val mCaptureRequestBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)

            //设置Surface作为预览数据的显示界面
            mCaptureRequestBuilder.addTarget(mSurface)
            mCaptureRequestBuilder.addTarget(mPreviewImageReader!!.surface)
            //创建相机捕获会话，第一个参数是捕获数据的输出Surface列表，第二个参数是CameraCaptureSession的状态回调接口，当它创建好后会回调onConfigured方法，
            // 第三个参数用来确定Callback在哪个线程执行，为null的话就在当前线程执行
            mCameraDevice!!.createCaptureSession(Arrays.asList(mSurface, mPreviewImageReader!!.surface, mPhotoImageReader!!.surface), object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    try {
                        //开始预览
                        mCameraCaptureSession = session
                        // 设置自动对焦模式
                        mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                        //设置反复捕获数据的请求，这样预览界面就会一直有数据显示
                        mCameraCaptureSession?.setRepeatingRequest(mCaptureRequestBuilder.build(), mCaptureCallBack, backgroundHandler)
                    } catch (e: CameraAccessException) {
                        e.printStackTrace()
                    }
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    Log.d("---------", "开启预览会话失败")
                }
            }, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }


    private val mCaptureCallBack = object : CameraCaptureSession.CaptureCallback() {

        override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest?, result: TotalCaptureResult) {
            super.onCaptureCompleted(session, request, result)
        }

        override fun onCaptureFailed(session: CameraCaptureSession?, request: CaptureRequest?, failure: CaptureFailure?) {
            super.onCaptureFailed(session, request, failure)
            Log.d("---------", "开启预览失败")
        }
    }


    fun getPreviewDatas(): ByteArray {
        return mdatas!!
    }


    private fun setCaptureOrientation(): Int? {
        // 获取设备方向
        val rotation = (mContext!!
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation
        val sensorOrientation = mCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)
        var deviceOrientation = ORIENTATIONS[rotation]
        if (mCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
            deviceOrientation = -deviceOrientation
        }

        return (ORIENTATIONS[deviceOrientation] + sensorOrientation!! + 270) % 360
    }

    /**
     * 停止拍照释放资源
     */
    fun closeCamera() {
        stopBackgroundThread()
        mCameraCaptureSession?.close()
        mCameraCaptureSession = null

        mCameraDevice?.close()
        mCameraDevice = null
        mPhotoImageReader?.close()
        mPhotoImageReader = null
        mPreviewImageReader?.close()
        mPreviewImageReader = null
    }


    private fun startBackgroundThread() {
        backgroundThread = HandlerThread("CameraBackground").also { it.start() }
        backgroundHandler = Handler(backgroundThread?.looper)
    }

    /**
     * Stops the background thread and its [Handler].
     */
    private fun stopBackgroundThread() {
        backgroundThread?.quitSafely()
        try {
            backgroundThread?.join()
            backgroundThread = null
            backgroundHandler = null
        } catch (e: InterruptedException) {
            Log.e(TAG, e.toString())
        }
    }

    /**
     * 切换摄像头
     */
    fun turnCamera() {
        //现在是后置，变更为前置
        closeCamera()
        mCameraId = if (mCameraId == "0") {
            "1"
        } else {
            "0"
        }
        openCamera(textureView!!.width, textureView!!.height)
    }
}
