package com.decard.zj.founctiontest.camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.decard.zj.founctiontest.R
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        CameraManager.getInstance().openCamera()

        btn_camera.setOnClickListener {
            iv_photo.setImageBitmap(CameraManager.getInstance().takePhoto())
        }
    }

    override fun onStart() {
        super.onStart()
        CameraManager.getInstance().startPreview(sv_picture.holder)
    }

    override fun onStop() {
        super.onStop()
        CameraManager.getInstance().close()
    }
}
