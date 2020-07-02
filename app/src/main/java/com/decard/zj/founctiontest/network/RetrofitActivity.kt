package com.decard.zj.founctiontest.network

import com.example.commonlibs.utils.FileUtils
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.decard.zj.founctiontest.R
import com.example.commonlibs.camera.CameraFactory
import com.example.commonlibs.camera.CameraUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_retrofit.*


class RetrofitActivity : AppCompatActivity() {

    var cameraUtils: CameraUtils? = null
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit)
        RxPermissions(this).requestEach(android.Manifest.permission.CAMERA)
                .subscribe {
                    val valuesHolder = PropertyValuesHolder.ofFloat("translationX", 0.0f, 0.0f)
                    val valuesHolder1 = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.3f, 1.0f)
                    val valuesHolder4 = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.3f, 1.0f)
                    val valuesHolder2 = PropertyValuesHolder.ofFloat("rotationX", 0.0f, 2 * 360.0f, 0.0f)
                    val valuesHolder5 = PropertyValuesHolder.ofFloat("rotationY", 0.0f, 2 * 360.0f, 0.0f)
                    val valuesHolder3 = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.7f, 1.0f)
                    val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(surface, valuesHolder, valuesHolder1, valuesHolder2, valuesHolder3, valuesHolder4, valuesHolder5)
                    objectAnimator.setDuration(5000).start()

                    cameraUtils = CameraFactory.openCamera(1)
                    cameraUtils!!.startCamera(surface, this)
                }
    }

    override fun onResume() {
        super.onResume()
        button2.setOnClickListener {
            //            val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.yan)
//            com.example.commonlibs.utils.FileUtils.saveMyBitmap("天行九歌", "焰灵姬", bitmap)
//            val oldPath = Environment.getExternalStorageDirectory().absolutePath + "/秦时明月/焰灵姬.png"
//            val file = File(oldPath)
//            var fileToBase64 = com.example.commonlibs.utils.FileUtils.fileToBase64(file)
//            SDCardUtil.saveToSDCard("base64.txt", fileToBase64!!)
//            Log.d("-------", fileToBase64)
        }

        button3.setOnClickListener {
            FileUtils.deleteDirectory(Environment.getExternalStorageDirectory().absolutePath + "/天行健")
        }
    }
}
