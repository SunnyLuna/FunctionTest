package com.decard.zj.founctiontest.wakelocktest

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import com.decard.zj.founctiontest.R
import kotlinx.android.synthetic.main.activity_wake_lock.*

/**
 * 应用只要申请了WakeLock,在释放wakeLock之前，系统不会进入休眠，
 * 即使在灭屏的情况下，应用要执行的任务依旧不会被系统打断
 */
class WakeLockActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wake_lock)
        val powerManager: PowerManager = getSystemService(Context.POWER_SERVICE) as PowerManager

        /*
          WakeLock等级
          /.PARTIAL_WAKE_LOCK,  灭屏，关闭键盘背光的情况下，CPU依然保持运行
          2.PROXIMITY_SCREEN_OFF_WAKE_LOCK  基于距离感应器熄灭屏幕，最典型的场景是贴近耳朵打电话，屏幕会自动熄灭
          3.ACQUIRE_CAUSES_WAKEUP  点亮屏幕，应用接收到通知后，屏幕亮起
          4.ON_AFTER_RELEASE  释放wakelock后，屏幕不马上熄灭
          5.SCREEN_DIM_WAKE_LOCK  保证屏幕亮起，但是亮度可能比较低，同时键盘背光也可以不亮
          6.SCREEN_BRIGHT_WAKE_LOCK   保证屏幕全亮
          7.FULL_WAKE_LOCK  使用最高亮度
         */
        val wakeLock: PowerManager.WakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "TAG")
//        wakeLock.setReferenceCounted(false)
        btn_wake.setOnClickListener {
            Thread {
                kotlin.run {
                    Thread.sleep(20000)
                    if (!powerManager.isScreenOn) {
                        wakeLock.acquire(5000)
                    }
                }
            }.start()

        }
    }
}
