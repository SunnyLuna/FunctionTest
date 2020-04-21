package com.example.commonlibs.utils;

import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.example.commonlibs.BaseApplication;

import java.util.Locale;

/**
 * 语音播报工具
 */
public class SpeechUtils implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private static final String TAG = "---SoundUtils";

    private static SpeechUtils instance;

    public SpeechUtils() {
        textToSpeech = new TextToSpeech(BaseApplication.instance, this);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "SpeechUtils: " + textToSpeech);
    }

    public static SpeechUtils getInstance() {
        if (instance == null) {
            instance = new SpeechUtils();
        }
        return instance;
    }


    /**
     * 用来初始化TextToSpeech引擎
     * status:SUCCESS或ERROR这2个值
     * setLanguage设置语言，帮助文档里面写了有22种
     * TextToSpeech.LANG_MISSING_DATA：表示语言的数据丢失。
     * TextToSpeech.LANG_NOT_SUPPORTED:不支持
     */
    @Override
    public void onInit(int status) {
        Log.d(TAG, "status: " + status);
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.CHINA);
            Log.d(TAG, "onInit: " + result);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.d(TAG, "onInit: " + "数据丢失或不支持");
            }
        }
    }

    public void playSound(String msg) {
        Log.d(TAG, "playSound: " + textToSpeech);
        Log.d(TAG, "playSound: " + msg);
        if (textToSpeech != null && !textToSpeech.isSpeaking()) {
            textToSpeech.setPitch(0.0f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
            textToSpeech.speak(msg,
                    TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    public void stopSound() {
        instance = null;
        textToSpeech.stop(); // 不管是否正在朗读TTS都被打断
        textToSpeech.shutdown(); // 关闭，释放资源
    }
}