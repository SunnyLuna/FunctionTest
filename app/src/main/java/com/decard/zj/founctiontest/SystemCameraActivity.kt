package com.decard.zj.founctiontest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_system_camera.*

class SystemCameraActivity : AppCompatActivity() {
    lateinit var zhaoWeiCamera: ZhaoWeiCameraUtils
    lateinit var zhaoWeiCameraFront: ZhaoWeiCameraUtils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_system_camera)
        RxPermissions(this).request(android.Manifest.permission.CAMERA).subscribe {
            zhaoWeiCamera = ZhaoWeiCameraUtils()
            zhaoWeiCamera.startCamera(textureView, this)
//            zhaoWeiCameraFront = ZhaoWeiCameraFront()
//            zhaoWeiCameraFront.startCamera(textureView1, this)
        }

    }

}
