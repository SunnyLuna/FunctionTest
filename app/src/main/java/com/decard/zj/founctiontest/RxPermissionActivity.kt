package com.decard.zj.founctiontest

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.functions.Consumer


class RxPermissionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_permission)

        //申请单个权限
        val rxPermissions = RxPermissions(this)
//        rxPermissions
//                .request(Manifest.permission.READ_PHONE_STATE)
//                .subscribe {
//                    if (it) {
//                        Toast.makeText(this, "同意了", Toast.LENGTH_SHORT).show()
//                        getIMEI()
//                    } else {
//                        Toast.makeText(this, "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show()
//                    }
//                }
        //同时申请多个权限
        var subscribe = RxPermissions(this).requestEach(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(Consumer<Permission> {
                    if (it.granted) {
                        getIMEI()
                        Toast.makeText(this, "同意了", Toast.LENGTH_SHORT).show()
                    } else if (it.shouldShowRequestPermissionRationale) {
                        Toast.makeText(this,
                                "Denied permission without ask never again",
                                Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this,
                                "Permission denied, can't enable the camera",
                                Toast.LENGTH_SHORT).show()
                    }
                })
    }

    @SuppressLint("MissingPermission")
    fun getIMEI() {
    val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val imeiId = telephonyManager.getImei(0)
        Log.d("---------", imeiId)
        val imeiIds = telephonyManager.getImei(1)
        Log.d("---------", imeiIds)
        val imeiIdss = telephonyManager.deviceId
        Log.d("---------", imeiIdss)
        Toast.makeText(this, imeiId, Toast.LENGTH_SHORT).show()
    }
}
