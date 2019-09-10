package com.decard.zj.founctiontest.download

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log


/**
 * @Author: liuwei
 *
 * @Create: 2019/5/15 16:47
 *
 * @Description:

 */
class AppBean {
    var appIcon: Drawable? = null
    var appName: String? = null
    var appSize: Long = 0
    var versionName: String? = null
    var versionCode = 0
    var appPackageName: String? = null
    var apkPath: String? = null

    override fun toString(): String {
        Log.e("APP", "APP:$appName AppName:$appPackageName versionName:$versionName versionCode:$versionCode")
        return super.toString()
    }
}