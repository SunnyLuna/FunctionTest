package com.example.commonlibs

import android.app.Application
import android.content.Context

/**
 *
 * @author ZJ
 * created at 2019/5/20 16:30
 */
open class BaseApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        context = applicationContext

    }

    companion object {
        private var context: Context? = null
        fun getAppContext(): Context {
            return context!!
        }
    }

}
