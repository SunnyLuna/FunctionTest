package com.example.commonlibs.apkinstaller

import android.graphics.drawable.Drawable


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
        return "AppBean(appIcon=$appIcon, appName=$appName, appSize=$appSize, versionName=$versionName, versionCode=$versionCode, appPackageName=$appPackageName, apkPath=$apkPath)"
    }


}