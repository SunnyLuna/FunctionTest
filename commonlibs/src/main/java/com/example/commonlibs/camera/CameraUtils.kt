package com.example.commonlibs.camera

import android.content.Context
import android.view.TextureView

abstract class CameraUtils {

    abstract fun startCamera(tv: TextureView, context: Context)

    abstract fun getPreviewDatas(): ByteArray

    abstract fun takePhoto(iPhotoInterface:IPhotoInterface)

    abstract fun turnCamera()

    abstract fun closeCamera()

}