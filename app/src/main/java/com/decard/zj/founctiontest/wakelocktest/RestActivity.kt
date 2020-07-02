package com.decard.zj.founctiontest.wakelocktest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.decard.zj.founctiontest.R
import kotlinx.android.synthetic.main.activity_rest.*

class RestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rest)

        //设置
        btn_rest_set.setOnClickListener {
            setScreenOffTime(10000)
            Log.d("-------", "休眠时间为：${getScreenOffTime()}")
        }
        //增加
        btn_rest_add.setOnClickListener {
            setScreenOffTime(15000)
            Log.d("-------", "休眠时间为：${getScreenOffTime()}")

        }
    }

    /**
     * 设置休眠时间   毫秒
     */
    private fun setScreenOffTime(time: Int) {
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, time)
    }

    /**
     * 获取休眠时间
     */

    private fun getScreenOffTime(): Int {
        return Settings.System.getInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT)
    }
}
