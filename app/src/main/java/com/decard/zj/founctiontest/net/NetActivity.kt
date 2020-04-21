package com.decard.zj.founctiontest.net

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.decard.zj.founctiontest.R
import com.example.commonlibs.net.NetChangeListener
import com.example.commonlibs.net.NetType
import com.example.commonlibs.net.NetWorkListener
import kotlinx.android.synthetic.main.activity_net.*

class NetActivity : AppCompatActivity(), NetChangeListener {


    override fun onConnect(netType: NetType?) {
        tv_net.text = netType.toString() + "网络连接"
    }

    override fun onDisConnect() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_net)
        NetWorkListener.getInstance().init()
        NetWorkListener.getInstance().setListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        NetWorkListener.getInstance().close()
    }
}
