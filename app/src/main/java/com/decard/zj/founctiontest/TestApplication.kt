package com.decard.zj.founctiontest

import com.example.commonlibs.BaseApplication
import com.example.commonlibs.utils.LogUtils

/**
 * File Description
 * @author Dell
 * @date 2018/10/8
 *
 */
class TestApplication : BaseApplication() {


    override fun onCreate() {
        super.onCreate()
        instance = this
        LogUtils.config(true, true)
//        MultiDex.install(this)
//        Realm.init(this)
//        val configuration = RealmConfiguration
//                .Builder()
//                .name("AppStore.realm")
//                .schemaVersion(1)
//                .build()
//        Realm.setDefaultConfiguration(configuration)

    }

    companion object {
        lateinit var instance: TestApplication
    }

}
