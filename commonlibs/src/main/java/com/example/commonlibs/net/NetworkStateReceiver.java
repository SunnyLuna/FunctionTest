package com.example.commonlibs.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NetworkStateReceiver extends BroadcastReceiver {

    private static final String TAG = "NetworkStateReceiver";
    private NetType netType;//网络类型
    private NetChangeListener listener;

    public NetworkStateReceiver() {
        //初始化网络连接状态
        this.netType = NetType.NONE;
    }

    public void setListener(NetChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Log.e(TAG, "onReceive: 异常");
            return;
        }
        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            Log.d(TAG, "onReceive: 网络发生变化");
            //获取当前联网的网络类型
            netType = NetUtils.getNetType();
            if (NetUtils.isNetworkAvailable()) {
                Log.d(TAG, "onReceive: 网络连接成功");
                if (listener != null) {
                    listener.onConnect(netType);
                }
            } else {
                Log.e(TAG, "onReceive: 网络连接失败");
                if (listener != null) {
                    listener.onDisConnect();
                }
            }
        }
    }
}
