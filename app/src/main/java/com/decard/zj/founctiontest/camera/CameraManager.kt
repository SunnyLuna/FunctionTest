package com.decard.zj.founctiontest.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.hardware.Camera
import android.util.Log
import android.view.SurfaceHolder

/**
 * File Description
 * @author Dell
 * @date 2018/9/28
 *
 */
class CameraManager {

    companion object {
        fun getInstance() = Handler.cameraManager
    }

    private object Handler {
        val cameraManager = CameraManager()
    }

    private var mCamera: Camera? = null

    fun openCamera() {
        mCamera = Camera.open()
        val parameter = mCamera!!.parameters
        parameter.pictureFormat = PixelFormat.JPEG//设置拍照后存储的图片格式
        parameter.setPreviewSize(640, 480)//设置预览图片大小
        parameter.setPictureSize(640, 480)
        mCamera!!.parameters = parameter
    }

    private var surfaceHolder: SurfaceHolder? = null
    /**
     * 开启预览
     */
    fun startPreview(holder: SurfaceHolder) {
        surfaceHolder = holder
        holder.addCallback(callback)
        mCamera!!.setPreviewDisplay(holder)
        mCamera!!.startPreview()

    }

    /**
     * 停止预览，关闭相机
     */
    fun close() {
        surfaceHolder!!.removeCallback(callback)
        mCamera!!.setPreviewCallback(null)
        mCamera!!.stopPreview()
        mCamera!!.release()
        mCamera = null
    }

    private val callback = object : SurfaceHolder.Callback {

        override fun surfaceCreated(holder: SurfaceHolder?) {
            previewCamera(holder!!)
            Log.d("---------", "surfaceCreated")
        }

        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            Log.d("---------", "surfaceChanged")
            mCamera!!.stopPreview()
            previewCamera(holder!!)
        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
            Log.d("---------", "surfaceDestroyed")
            mCamera!!.setPreviewCallback(null)
            mCamera!!.stopPreview()
            mCamera!!.release()
            mCamera = null
        }
    }


    private fun previewCamera(holder: SurfaceHolder) {
        //摄像头设置SurfaceHolder对象，将摄像头与surfaceHolder进行绑定
        mCamera!!.setPreviewDisplay(surfaceHolder)
        //调用摄像机预览功能
        mCamera!!.startPreview()
        //实时获取预览数据
        mCamera!!.setPreviewCallback(Camera.PreviewCallback { data, camera ->
            Log.d("---------", "data : ${data.size}")
        })
    }

    /**
     * 拍照
     */
    private var bitmap: Bitmap? = null

    fun takePhoto(): Bitmap {

        Log.d("--------", "${mCamera}")
        mCamera!!.takePicture(null, null, Camera.PictureCallback { data, camera ->
            Log.d("--------", "${data.size}")
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            camera.startPreview()
        })
        return bitmap!!
    }
}