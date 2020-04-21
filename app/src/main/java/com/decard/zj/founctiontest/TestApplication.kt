package com.decard.zj.founctiontest

import com.example.commonlibs.BaseApplication
import io.realm.Realm
import io.realm.RealmConfiguration

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
//        MultiDex.install(this)
        Realm.init(this)
        val configuration = RealmConfiguration
                .Builder()
                .name("AppStore.realm")
                .schemaVersion(1)
                .build()
        Realm.setDefaultConfiguration(configuration)

    }

    companion object {
        lateinit var instance: TestApplication
    }

}
