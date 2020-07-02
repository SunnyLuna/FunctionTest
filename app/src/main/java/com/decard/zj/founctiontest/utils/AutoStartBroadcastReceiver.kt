package com.decard.zj.founctiontest.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import com.decard.zj.founctiontest.service.AutoService
import org.slf4j.LoggerFactory


class AutoStartBroadcastReceiver : BroadcastReceiver() {

    val logger = LoggerFactory.getLogger("AutoStartBroadcastReceiver")

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        logger.trace(action)
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
            logger.trace("开机自启")
            startActivity(context)
        }
    }

    private fun startActivity(context: Context) {

        val startSelfIntent = Intent(context, AutoService::class.java)
        context.startService(startSelfIntent)
    }
}
