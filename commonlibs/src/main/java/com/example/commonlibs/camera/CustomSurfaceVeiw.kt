package com.example.commonlibs.camera

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.hardware.Camera
import android.util.Log
import android.view.*
import com.example.commonlibs.pickview.ScreenUtils
import java.io.ByteArrayOutputStream

/**
 * 自定义camera1
 * @author ZJ
 * created at 2019/5/9 14:26
 */
class CustomSurfaceVeiw {
    private var mCamera: Camera? = null
    private lateinit var mContext: Context
    private lateinit var mSurfaceView: SurfaceView
    private lateinit var mSurfaceHolder: SurfaceHolder
    private var mDisplayOrientation = 0
    private var mDatas: ByteArray? = null
    private var mCameraId: Int = 0
    private var mWidth = 0
    private var mHeight = 0

    /**
     * 创建单例模式
     */
    companion object {
        val instance: CustomSurfaceVeiw get() = Holder.customCamera
    }

    //通过object创建内部对象
    private object Holder {
        val customCamera = CustomSurfaceVeiw()
    }

    /**
     * 打开照相机
     */
    fun openCamera(context: Context) {
        this.mContext = context
        mCamera = Camera.open()
        setParameters()
    }

    /**.
     * 设置照相机的相关参数
     */
    private fun setParameters() {
        if (mCamera != null) {
            val params = mCamera!!.parameters//获取camera参数
            params.pictureFormat = PixelFormat.JPEG  //设置图片格式
            params.previewFormat = ImageFormat.NV21  //设置预览的图片格式
            //设置预览尺寸
            val bestPreviewSize = getBestCameraResolution(ScreenUtils.widthPixels(mContext), ScreenUtils.heightPixels(mContext), params.supportedPreviewSizes)
            mWidth = bestPreviewSize!!.width
            mHeight = bestPreviewSize.height
            params.setPreviewSize(mWidth, mHeight)
            //设置照片尺寸
            val bestPictureSize = getBestCameraResolution(ScreenUtils.widthPixels(mContext), ScreenUtils.heightPixels(mContext), params.supportedPictureSizes)
            params.setPictureSize(bestPictureSize!!.width, bestPictureSize.height)
            mCamera!!.parameters = params
        }
    }

    /**
     * 开始预览
     */
    fun startPreview(surfaceView: SurfaceView) {
        this.mSurfaceView = surfaceView
        if (mCamera != null) {
            mSurfaceHolder = mSurfaceView.holder
            mSurfaceHolder.addCallback(surfaceCallback)
            mCamera!!.setPreviewDisplay(mSurfaceHolder)
            setCameraDisplayOrientation()
            mCamera!!.startPreview()
            mCamera!!.setPreviewCallback { data, _ ->
                this.mDatas = data
            }
        }
        //点击预览界面  自动对焦
        mSurfaceView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (mCamera != null) {
                    mCamera!!.autoFocus(autoFocusCallback)
                }
                return false
            }

        })
    }

    //设置预览旋转的角度
    private fun setCameraDisplayOrientation() {
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(0, info)
        val rotation = (mContext as Activity).windowManager.defaultDisplay.rotation

        var screenDegree = 0
        when (rotation) {
            Surface.ROTATION_0 -> screenDegree = 0
            Surface.ROTATION_90 -> screenDegree = 90
            Surface.ROTATION_180 -> screenDegree = 180
            Surface.ROTATION_270 -> screenDegree = 270
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mDisplayOrientation = (info.orientation + screenDegree) % 360
            mDisplayOrientation = (360 - mDisplayOrientation) % 360          // compensate the mirror
        } else {
            mDisplayOrientation = (info.orientation - screenDegree + 360) % 360
        }
        mCamera!!.setDisplayOrientation(mDisplayOrientation)
    }

    /**
     * 获取最佳的尺寸大小
     * 宽高相等或最接近的尺寸
     */
    private fun getBestCameraResolution(width: Int, height: Int, sizeList: List<Camera.Size>): Camera.Size? {
        var bestSize: Camera.Size? = null
        val targetRatio = (width.toDouble() / height)  //目标大小的宽高比
        var minDiff = targetRatio

        for (size in sizeList) {
            if (size.width == width && size.height == height) {
                bestSize = size
                break
            }
            val supportedRatio = size.width.toDouble() / size.height
            if (Math.abs(supportedRatio - targetRatio) < minDiff) {
                minDiff = Math.abs(supportedRatio - targetRatio)
                bestSize = size
            }
        }

        return bestSize
    }


    private val surfaceCallback = object : SurfaceHolder.Callback {
        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            Log.d("----------", "surfaceChanged")
        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
            Log.d("----------", "surfaceDestroyed")
        }

        override fun surfaceCreated(holder: SurfaceHolder?) {
            Log.d("----------", "surfaceCreated")
            startPreview(mSurfaceView)
        }
    }
    private var autoFocusCallback = object : Camera.AutoFocusCallback {
        override fun onAutoFocus(success: Boolean, camera: Camera?) {
            Log.d("------------", success.toString())
        }

    }

    fun getPreviewDatas(): Bitmap {
        val image = YuvImage(mDatas, ImageFormat.NV21, mWidth, mHeight, null)
        val stream = ByteArrayOutputStream()
        image.compressToJpeg(Rect(0, 0, mWidth, mHeight), 80, stream)
        val bitmap = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size())
        return if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT)
            rotate(bitmap, 270f)  //前置摄像头旋转270°
        else
            rotate(bitmap, 90f)
    }


    fun setPhotoListener(iPhotoInterface: IPhotoInterface) {

        mCamera!!.takePicture(null, null, Camera.PictureCallback { data, _ ->
            Log.d("--------", mDisplayOrientation.toString())
            var bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            bitmap = if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT)
                rotate(bitmap, 270f)
            else
                rotate(bitmap, mDisplayOrientation.toFloat())
            iPhotoInterface.getBitmap(bitmap)
            startPreview(mSurfaceView)
        })
    }

    interface IPhotoInterface {
        fun getBitmap(bitmap: Bitmap)
    }

    fun closeCamera() {
        if (mCamera != null) {
            mCamera!!.stopPreview()
            mCamera!!.setPreviewCallback(null)
            mCamera!!.release()
            mCamera = null
        }
    }

    /**
     * 切换摄像头
     */
    fun turnCamera(surfaceView: SurfaceView) {
        //现在是后置，变更为前置
        if (mCamera != null && mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            closeCamera()
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)//打开当前选中的摄像头

            mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT

        } else if (mCamera != null && mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) { //代表摄像头的方位，CAMERA_FACING_FRONT前置
            //现在是前置， 变更为后置
            closeCamera()
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK
        }
        setParameters()
        startPreview(surfaceView)
    }

    //水平镜像翻转
    fun mirror(rawBitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postScale(-1f, 1f)
        return Bitmap.createBitmap(rawBitmap, 0, 0, rawBitmap.width, rawBitmap.height, matrix, true)
    }

    //旋转
    fun rotate(rawBitmap: Bitmap, degree: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree)
        return Bitmap.createBitmap(rawBitmap, 0, 0, rawBitmap.width, rawBitmap.height, matrix, true)
    }
}
