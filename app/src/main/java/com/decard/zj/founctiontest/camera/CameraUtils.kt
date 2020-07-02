package com.decard.zj.founctiontest.camera

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.CameraManager
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.ImageReader
import android.util.Log
import android.util.Size
import android.util.SparseArray
import android.view.Surface
import android.view.TextureView
import com.decard.zj.founctiontest.TestApplication
import com.decard.zj.founctiontest.serialport.RxBusInner
import java.util.*

/**
 * File Description
 * @author Dell
 * @date 2018/9/28
 *
 */
class CameraUtils {

    private val TAG = "---ZhaoWeiCameraUtils"

    companion object {
        fun getInstance() = Holder.cameraUtils
    }

    private object Holder {
        val cameraUtils = CameraUtils()
    }

    private val cameraId = "0"

    private var imageReader: ImageReader? = null
    private var mHeight = 0
    private var mWidth = 0
    private var previewSize: Size? = null

    private var mPreviewSession: CameraCaptureSession? = null
    private var mCaptureRequestBuilder: CaptureRequest.Builder? = null
    private var captureRequestBuilder: CaptureRequest.Builder? = null
    private var mCaptureRequest: CaptureRequest? = null

    private var mTextureView: TextureView? = null
    private var mActivity: Activity? = null
    private var mCameraDevice: CameraDevice? = null

    @SuppressLint("UseSparseArrays")
    private val ORIENTATIONS = SparseArray<Int>()

    /**
     * 启动拍照
     */
    fun startCamera(activity: Activity, textureView: TextureView) {
        mActivity = activity
        mTextureView = textureView
        ORIENTATIONS.append(Surface.ROTATION_0, 90)
        ORIENTATIONS.append(Surface.ROTATION_90, 0)
        ORIENTATIONS.append(Surface.ROTATION_180, 270)
        ORIENTATIONS.append(Surface.ROTATION_270, 180)
        if (textureView.isAvailable) {
            if (null == mCameraDevice) {
                openCamera()
            }
        } else {
            textureView.surfaceTextureListener = surfaceTextureListener
        }
    }

    /**
     * 打开摄像头
     */
    @SuppressLint("MissingPermission")
    private fun openCamera() {
        val cameraManager: CameraManager = TestApplication.instance.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val list = cameraManager.cameraIdList
        Log.d(TAG, "list': ${list.size}");
        //设置摄像头特性
//        setCameraCharacters(cameraManager)
//        cameraManager.openCamera(cameraId, stateCallback, null)


    }

    /**
     * 设置摄像头的参数
     */
    private fun setCameraCharacters(cameraManager: CameraManager) {

        //获取指定摄像头的特性
        val characteristics = cameraManager.getCameraCharacteristics(cameraId)
        //获取摄像头支持的配置属性
        val map: StreamConfigurationMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        //获取摄像头支持的最大尺寸
        val largest = Collections.max(
                Arrays.asList(*map.getOutputSizes(ImageFormat.JPEG)), CompareSizesByArea())
        //创建一个ImageReader对象，用于获取摄像头的图像数据
        imageReader = ImageReader.newInstance(largest.width, largest.height, ImageFormat.JPEG, 2)
        //设置获取图片的监听
        imageReader!!.setOnImageAvailableListener(imageAvailableListener, null)
        //获取最佳的预览尺寸
        previewSize = chooseOptimalSize(map.getOutputSizes(
                SurfaceTexture::class.java), mWidth, mHeight, largest)

    }



    class CompareSizesByArea : Comparator<Size> {
        override fun compare(lhs: Size?, rhs: Size?): Int {
            // 强转为long保证不会发生溢出
            return java.lang.Long.signum(lhs!!.width.toLong() * lhs.height - rhs!!.width.toLong() * rhs.height)
        }

    }

    /**
     * TextureView的监听
     */
    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {

        //可用
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            mWidth = width
            mHeight = height
            Log.d(TAG, "textureView的监听: width=$width  height=$height")
            openCamera()
        }


        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {

        }

        //释放
        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            stopCamera()
            return true
        }

        //更新
        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

        }
    }


    /**
     * 拍照
     */
    fun takePicture() {
        try {
            if (mCameraDevice == null) {
                return
            }
            // 创建拍照请求
            captureRequestBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            // 设置自动对焦模式
            captureRequestBuilder!!.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            // 将imageReader的surface设为目标
            captureRequestBuilder!!.addTarget(imageReader!!.getSurface())
            // 获取设备方向
            val rotation = mActivity!!.getWindowManager().getDefaultDisplay().getRotation()
            // 根据设备方向计算设置照片的方向
            captureRequestBuilder!!.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation))
            // 停止连续取景   停止预览
            mPreviewSession!!.stopRepeating()
            //拍照
            val captureRequest = captureRequestBuilder!!.build()
            //设置拍照监听   执行拍照动作
            mPreviewSession!!.capture(captureRequest, captureCallback, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

    }

    /**
     * 监听拍照结果
     */
    private val captureCallback = object : CameraCaptureSession.CaptureCallback() {
        // 拍照成功
        override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
            // 重设自动对焦模式
            captureRequestBuilder!!.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL)
            // 设置自动曝光模式
            captureRequestBuilder!!.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
            try {
                //重新进行预览
                mPreviewSession!!.setRepeatingRequest(mCaptureRequest, null, null)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }

        }

        override fun onCaptureFailed(session: CameraCaptureSession, request: CaptureRequest, failure: CaptureFailure) {
            super.onCaptureFailed(session, request, failure)
        }
    }


    private var bitmap: Bitmap? = null
    /**
     * 监听拍照的图片
     */
    private val imageAvailableListener = ImageReader.OnImageAvailableListener { reader ->
        // 当照片数据可用时激发该方法
        // 获取捕获的照片数据
        val image = reader.acquireNextImage()
        val buffer = image.planes[0].buffer
        val data = ByteArray(buffer.remaining())
        buffer.get(data)
        //显示图片
        val options = BitmapFactory.Options()
        options.inSampleSize = 2
        Log.d(TAG, "onImageAvailable: " + data.size)
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.size, options)
        Log.d(TAG, "onImageAvailable: " + bitmap!!)
        RxBusInner.getInstance().post(bitmap!!)
        image.close()
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
    private fun chooseOptimalSize(choices: Array<Size>, width: Int, height: Int, aspectRatio: Size): Size {
        // 收集摄像头支持的大过预览Surface的分辨率
        val bigEnough = ArrayList<Size>()
        val w = aspectRatio.width
        val h = aspectRatio.height
        for (option in choices) {
            if (option.height == option.width * h / w &&
                    option.width >= width && option.height >= height) {
                bigEnough.add(option)
            }
        }
        // 如果找到多个预览尺寸，获取其中面积最小的
        return if (bigEnough.size > 0) {
            Collections.min(bigEnough, CompareSizesByArea())
        } else {
            //没有合适的预览尺寸
            choices[0]
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
     * 开始预览
     */
    private fun takePreview() {
        val surfaceTexture = mTextureView!!.getSurfaceTexture()
        //设置TextureView的缓冲区大小
        surfaceTexture.setDefaultBufferSize(previewSize!!.getWidth(), previewSize!!.getHeight())
        //获取Surface显示预览数据
        val mSurface = Surface(surfaceTexture)
        try {
            //创建预览请求
            mCaptureRequestBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            // 设置自动对焦模式
            mCaptureRequestBuilder!!.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            //设置Surface作为预览数据的显示界面
            mCaptureRequestBuilder!!.addTarget(mSurface)
            //创建相机捕获会话，第一个参数是捕获数据的输出Surface列表，第二个参数是CameraCaptureSession的状态回调接口，当它创建好后会回调onConfigured方法，第三个参数用来确定Callback在哪个线程执行，为null的话就在当前线程执行
            mCameraDevice!!.createCaptureSession(Arrays.asList(mSurface, imageReader!!.getSurface()), object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    try {
                        //开始预览
                        mCaptureRequest = mCaptureRequestBuilder!!.build()
                        mPreviewSession = session
                        //设置反复捕获数据的请求，这样预览界面就会一直有数据显示
                        mPreviewSession!!.setRepeatingRequest(mCaptureRequest, null, null)
                    } catch (e: CameraAccessException) {
                        e.printStackTrace()
                    }

                }

                override fun onConfigureFailed(session: CameraCaptureSession) {

                }
            }, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

    }

    /**
     * 停止拍照释放资源
     */
    private fun stopCamera() {
        if (mCameraDevice != null) {
            mCameraDevice!!.close()
            mCameraDevice = null
        }
    }

}