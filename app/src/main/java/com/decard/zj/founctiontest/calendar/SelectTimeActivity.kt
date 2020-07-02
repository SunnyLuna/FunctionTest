package com.decard.zj.founctiontest.calendar

import android.os.Bundle
import com.decard.zj.founctiontest.R
import com.example.commonlibs.mvp.activity.BaseCompatActivity
import com.orhanobut.logger.Logger
import com.tbruyelle.rxpermissions2.RxPermissions


class SelectTimeActivity : BaseCompatActivity() {
    private val TAG = "---SelectTimeActivity"
    override fun getLayoutId(): Int {
        return R.layout.activity_select_time
    }

    override fun initView(savedInstanceState: Bundle?) {
        if (findFragment(TestFragment::class.java) == null) {
            loadRootFragment(R.id.fl_container, TestFragment.newInstance())
        }
        RxPermissions(this).request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe {

        }
        Logger.t(TAG).e("你好啊")
        val json = "{\"type\":\"AH_RYJBXX\",\"AAC001\":\"34010000100000123456\",\"AAC002\":\"苏景芳\",\"AAC003\":\"341281197305235004\",\"AAC004\":\"1\",\"AAC009\":\"电焊工\",\"AAC006\":\"水帘洞\"}"
        Logger.t(TAG).json(json)
        Logger.t(TAG).xml("")


//        Logger.addLogAdapter(AndroidLogAdapter());
//        Logger.d("message");
//
//        Logger.clearLogAdapters();
//
//
//        var formatStrategy = PrettyFormatStrategy.newBuilder()
//                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
//                .methodCount(0)         // (Optional) How many method line to show. Default 2
//                .methodOffset(3)        // (Optional) Skips some method invokes in stack trace. Default 5
////        .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
//                .tag("My custom tag")   // (Optional) Custom tag for each log. Default PRETTY_LOGGER
//                .build();
//
//        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy));
//
//        Logger.addLogAdapter(object : AndroidLogAdapter() {
//
//            override fun isLoggable(priority: Int, tag: String?): Boolean {
//                return BuildConfig.DEBUG
//            }
//        });
//
//        Logger.addLogAdapter(DiskLogAdapter());
//
//
//        Logger.w("no thread info and only 1 method");
//
//        Logger.clearLogAdapters();
//        formatStrategy = PrettyFormatStrategy.newBuilder()
//                .showThreadInfo(false)
//                .methodCount(0)
//                .build()
//
//        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
//        Logger.i("no thread info and method info")
//
//        Logger.t("tag").e("Custom tag for only one use")
//
//        Logger.json("{ \"key\": 3, \"value\": something}")
//
//        Logger.d(listOf("foo", "bar"))
//
//        val map = HashMap<String, String>()
//        map["key"] = "value"
//        map["key1"] = "value2"
//
//        Logger.d(map);
//
//        Logger.clearLogAdapters();
//        formatStrategy = PrettyFormatStrategy.newBuilder()
//                .showThreadInfo(false)
//                .methodCount(0)
//                .tag("MyTag")
//                .build()
//        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
//
//        Logger.w("my log message with my tag")
    }
}
