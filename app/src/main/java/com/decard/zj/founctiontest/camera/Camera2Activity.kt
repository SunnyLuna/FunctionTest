package com.decard.zj.founctiontest.camera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.decard.zj.founctiontest.R
import com.decard.zj.kotlinbaseapplication.utils.RxBusInner
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_camera2.*

class Camera2Activity : AppCompatActivity() {

    private val RESULT_CODE_CAMERA = 1//判断是否有拍照权限的标识码
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera2)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            val perms = arrayOf("android.permission.CAMERA")
            ActivityCompat.requestPermissions(this, perms, RESULT_CODE_CAMERA)
        } else {
            CameraUtils.getInstance().startCamera(this, textureView_photo)
        }
        button_take_photo.setOnClickListener {
            CameraUtils.getInstance().takePicture()
        }

        RxBusInner.getInstance().toObservable(Bitmap::class.java).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<Bitmap> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(bitmap: Bitmap) {
                image_photo.setImageBitmap(bitmap)
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        })
        Thread(Runnable {
            while (true) {
                if (textureView_photo.bitmap != null) {
                    Log.d("--------", textureView_photo.bitmap.toString())
                }
            }
        }).start()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            RESULT_CODE_CAMERA -> {
                var cameraAccept = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (cameraAccept) {
                    //授权成功后，调用系统相机
                    CameraUtils.getInstance().startCamera(this, textureView_photo)
                } else {
                    Toast.makeText(this, "请开启相机权限", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
