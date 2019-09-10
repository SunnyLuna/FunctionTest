package com.decard.zj.founctiontest.utils

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.decard.zj.founctiontest.R
import kotlinx.android.synthetic.main.activity_sound.*

class SoundActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound)
        TTSUtils.getInstance().init(this)
        TTSUtils.getInstance().changeEngine("com.iflytek.speechcloud")
    }

    var isAndroid: Boolean = false
    override fun onResume() {
        super.onResume()
//        SpeechUtils.getInstance().playSound("13456")
        btn_play.setOnClickListener {
            //            if (isAndroid) {
//                isAndroid = false
                TTSUtils.getInstance().speak("大河向东流啊")
//            } else {
//                isAndroid = true
//            SpeechUtils.getInstance().playSound("大河向东流啊")
//            }

        }

    }

    override fun onStop() {
        super.onStop()
        TTSUtils.getInstance().release()
    }
}
