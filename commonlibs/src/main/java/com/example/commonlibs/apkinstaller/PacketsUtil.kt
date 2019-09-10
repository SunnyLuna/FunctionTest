package com.example.commonlibs.apkinstaller

import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.widget.Toast
import java.io.File

/**
 * 打开应用
 * @author ZJ
 * created at 2019/8/9 14:47
 */
object PacketsUtil {
    fun getAllApk(context: Context): ArrayList<AppBean> {
        val appBeanList = ArrayList<AppBean>()
        var bean: AppBean?
        val packageManager = context.packageManager
        val list = packageManager.getInstalledPackages(0)
        for (p in list) {
            bean = AppBean()
            bean.appIcon = p.applicationInfo.loadIcon(packageManager)
            bean.appName = packageManager.getApplicationLabel(p.applicationInfo).toString()
            bean.appPackageName = p.applicationInfo.packageName
            bean.apkPath = p.applicationInfo.sourceDir
            bean.versionName = p.versionName
            bean.versionCode = p.versionCode
            val file = File(p.applicationInfo.sourceDir)
            bean.appSize = file.length()
            val flags = p.applicationInfo.flags
            bean.appIcon = packageManager.getApplicationIcon(p.applicationInfo)


            if (flags and ApplicationInfo.FLAG_SYSTEM != 0) {
            } else {
                if (bean.appPackageName!!.contains("decard")) {
                    appBeanList.add(bean)
                    bean.toString()
                }
            }
        }
        return appBeanList
    }

    fun openApp(mContext: Context?, mPacketName: String?) {
        if (mContext == null || mPacketName.isNullOrEmpty()) {
            return
        }

        val packageManager = mContext.packageManager
        val launchIntentForPackage = packageManager?.getLaunchIntentForPackage(mPacketName)
        if (launchIntentForPackage != null)
            mContext.startActivity(launchIntentForPackage)
        else
            Toast.makeText(mContext, "手机未安装该应用", Toast.LENGTH_SHORT).show()
    }

    fun getVersion(activity: Activity): Int {
        val pkg: PackageInfo
        var versionCode = 0
        val versionName = ""
        try {
            pkg = activity.packageManager.getPackageInfo(activity.application.packageName, 0)
            versionCode = pkg.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return versionCode
    }


    fun getPackageVersion(context: Context): String? {
        val manager = context.packageManager
        var version: String? = null
        try {
            val info = manager.getPackageInfo(context.packageName, 0)
            version = info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return version
    }


    fun getPackageVersion(context: Context, packageName: String): String? {
        val manager = context.packageManager
        val version: String?
        try {
            val info = manager.getPackageInfo(packageName, 0)
            version = info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            return null
        }
        return version
    }

}