package com.example.commonlibs.net

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.example.commonlibs.BaseApplication


object NetUtils {


    /**
     * 判断网络是否连接
     *
     * @return
     */
    @JvmStatic
    fun isNetworkAvailable(): Boolean {

        val connectivityManager =
                BaseApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null) {
            return true
        }
        return false
    }


    /**
     * 获取当前网络类型
     *
     * @return NetType
     */
    @JvmStatic
    fun getNet(): String {
        val connMgr = BaseApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo ?: return "NULL"
        return when (networkInfo.type) {
            ConnectivityManager.TYPE_MOBILE -> if ("cmnet" == networkInfo.extraInfo.toLowerCase()) {
                "CMNET"
            } else {
                "CMWAP"
            }
            ConnectivityManager.TYPE_WIFI -> "WIFI"
            ConnectivityManager.TYPE_ETHERNET -> "ETH"
            else -> "NULL"
        }
    }

    /**
     * 获取当前网络类型
     *
     * @return NetType
     */
    @JvmStatic
    fun getNetType(): NetType {
        val connMgr = BaseApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo ?: return NetType.NONE
        val nType = networkInfo.type
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            return if ("cmnet" == networkInfo.extraInfo.toLowerCase()) {
                NetType.CMNET
            } else {
                NetType.CMWAP
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI
        } else if (nType == ConnectivityManager.TYPE_ETHERNET) {
            return NetType.ETH
        }
        return NetType.NONE
    }

    /**
     * 判断是否是wifi连接
     */
    fun isWifi(context: Context): Boolean {
        val cm = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                ?: return false

        return cm.activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI

    }

    /**
     * 打开网络设置界面
     */
    fun openSetting(activity: Activity) {
        val intent = Intent("/")
        val cm = ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings")
        intent.component = cm
        intent.action = "android.intent.action.VIEW"
        activity.startActivityForResult(intent, 0)
    }
}
