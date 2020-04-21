package com.example.commonlibs.net;

public interface NetChangeListener {
    /**
     * 已连接
     * @param netType NetType
     */
    void onConnect(NetType netType);

    /**
     * 连接断开
     */
    void onDisConnect();
}
