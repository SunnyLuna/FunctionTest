package com.decard.zj.founctiontest.network

interface LoadTasksCallback {

    fun onTasksLoaded(tasks: SocialBean)

    fun onDataNotAvailable(msg: String)

    fun onGetName(message:String)
}