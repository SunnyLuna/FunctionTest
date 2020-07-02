package com.example.commonlibs

import android.app.Activity
import android.app.Application


/**
 *
 * @author ZJ
 * created at 2019/5/20 16:30
 */
open class BaseApplication : Application() {

    lateinit var activityList: MutableList<Activity>

    override fun onCreate() {
        super.onCreate()

        instance = this
        activityList = ArrayList()
    }


    companion object {
        lateinit var instance: BaseApplication
    }


    open fun addActivity(activity: Activity?) {
        if (!activityList.contains(activity)) {
            activityList.add(activity!!)
        }
    }

    open fun removeActivity(activity: Activity) {
        if (activityList.contains(activity)) {
            activityList.remove(activity)
            activity.finish()
        }
    }


    open fun removeALLActivity() {
        for (activity in activityList) {
            activity.finish()
        }
    }

}
