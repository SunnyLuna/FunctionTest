package com.example.commonlibs.camera

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.hardware.Camera
import android.util.Log
import android.view.Surface
import android.view.TextureView
import com.example.commonlibs.pickview.ScreenUtils
import java.io.ByteArrayOutputStream

/**
 * 自定义camera1
 * @author ZJ
 * created at 2019/5/9 14:26
 */
class CustomCamera1 : CameraUtils() {


    private var mCamera: Camera? = null
    private lateinit var mContext: Context
    private var mDisplayOrientation = 0
    private var mDatas: ByteArray? = null
    private var mCameraId = 0
    private var mWidth = 0
    private var mHeight = 0

    private var textureView: TextureView? = null

    /**
     * 创建单例模式
     */
    companion object {
        val instance: CustomCamera1 get() = Holder.customCamera
    }

    //通过object创建内部对象
    private object Holder {
        val customCamera = CustomCamera1()
    }

    /**
     * 打开摄像头
     */
    override fun startCamera(tv: TextureView, context: Context) {
        this.mContext = context
        textureView = tv
        if (tv.isAvailable) {
            openCamera()
            Log.d("------------", "进来了")
        } else {
            Log.d("------------", "没进")
            tv.surfaceTextureListener = surfaceTextureListener
        }
    }

    /**
     * 打开照相机
     */
    private fun openCamera() {
        mCamera = Camera.open(mCameraId)
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
            params.set("preview-flip", "flip-v")
            Log.d("------", params.flatten())
            //设置照片尺寸
            val bestPictureSize = getBestCameraResolution(ScreenUtils.widthPixels(mContext), ScreenUtils.heightPixels(mContext), params.supportedPictureSizes)
            params.setPictureSize(bestPictureSize!!.width, bestPictureSize.height)
            mCamera!!.parameters = params
        }
        setCameraDisplayOrientation()
    }

    /**
     * 开始预览
     */
    private fun startPreview() {

        if (mCamera != null) {
            mCamera!!.setPreviewTexture(textureView?.surfaceTexture)
            mCamera!!.startPreview()
            mCamera!!.setPreviewCallback { data, _ ->
                this.mDatas = data
            }
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
            openCamera()
            startPreview()
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


    private var autoFocusCallback = object : Camera.AutoFocusCallback {
        override fun onAutoFocus(success: Boolean, camera: Camera?) {
            Log.d("------------", success.toString())
        }

    }

    fun getPreviewBitmap(): Bitmap {
        val image = YuvImage(mDatas, ImageFormat.NV21, mWidth, mHeight, null)
        val stream = ByteArrayOutputStream()
        image.compressToJpeg(Rect(0, 0, mWidth, mHeight), 80, stream)
        val bitmap = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size())
        return if (mCameraId == 1)
            rotate(bitmap, 270f)  //前置摄像头旋转270°
        else
            rotate(bitmap, 90f)
    }

    override fun getPreviewDatas(): ByteArray {
        return mDatas!!
    }


    override fun takePhoto(iPhotoInterface: IPhotoInterface) {

        mCamera!!.takePicture(null, null, Camera.PictureCallback { data, _ ->
            Log.d("--------", mDisplayOrientation.toString())
            val srcBitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            val matrix = Matrix()
            if (mCameraId == 0) {
                matrix.postRotate(mDisplayOrientation.toFloat())
            } else {
                matrix.postRotate(270f)
                matrix.postScale(-1f, 1f)
            }
            val bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.width, srcBitmap.height, matrix, true)
            iPhotoInterface.getBitmap(bitmap)
        })
    }

    override fun closeCamera() {
        if (mCamera != null) {
            mCamera?.setPreviewCallback(null)
            mCamera!!.stopPreview()
            mCamera!!.release()
            mCamera = null
        }
    }

    /**
     * 切换摄像头
     */
    override fun turnCamera() {
        if (mCamera != null && mCameraId == 1) {
            closeCamera()
            mCameraId = 0

        } else if (mCamera != null && mCameraId == 0) {
            closeCamera()
            mCameraId = 1
        }
        openCamera()
        startPreview()
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
