package com.decard.zj.founctiontest.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import org.slf4j.LoggerFactory

class AutoService : Service() {

    val logger = LoggerFactory.getLogger("AutoService")
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onCreate() {
        super.onCreate()
        logger.trace("service onCreate")
    }


    override fun onDestroy() {
        super.onDestroy()
        logger.trace("service onDestroy")
    }
}