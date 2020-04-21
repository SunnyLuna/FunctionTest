package com.example.commonlibs.net;

import android.content.IntentFilter;

import com.example.commonlibs.BaseApplication;

public class NetWorkListener {
    private NetworkStateReceiver receiver;

    public NetWorkListener() {
        receiver = new NetworkStateReceiver();
    }

    public static NetWorkListener getInstance() {
        return Holder.netWorkListener;
    }

    private static class Holder {
        static NetWorkListener netWorkListener = new NetWorkListener();
    }


    public void setListener(NetChangeListener listener) {
        receiver.setListener(listener);
    }

    public void init() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        BaseApplication.instance.registerReceiver(receiver, filter);
    }

    public void close() {
        BaseApplication.instance.unregisterReceiver(receiver);
    }


}
