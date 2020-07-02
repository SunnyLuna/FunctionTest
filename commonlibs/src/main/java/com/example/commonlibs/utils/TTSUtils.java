package com.example.commonlibs.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

public class TTSUtils {

    private TextToSpeech textToSpeech;
    private static volatile TTSUtils mTTSUtils;
    private static Context context;
    private static boolean bSwitch = true;
    private static final String TAG = "----TTSUtils";

    public static void config(boolean b) {
        bSwitch = b;
    }

    public static TTSUtils getInstance(){
        if(mTTSUtils==null){
            synchronized (TTSUtils.class){
                mTTSUtils = new TTSUtils();
            }
        }
        return mTTSUtils;
    }

    public void init(Context c){
        this.context = c;
        createTTSCHINA(c,null);
    }


    public List<TextToSpeech.EngineInfo> getEngines() {
        return textToSpeech.getEngines();
    }


    public String getDefaultEngine() {
        return textToSpeech.getDefaultEngine();
    }

    public int speak(String text){
        if(bSwitch){
            return textToSpeech.speak(text,TextToSpeech.QUEUE_ADD,null);
        }
        return -1;

    }


    public void changeEngine(String name){
        createTTSCHINA(context,name);
    }


    private void createTTSCHINA(Context context,String name){
        if(name == null){

            //生成默认引擎的speech
            textToSpeech = new TextToSpeech(context,status->{

                if(status==TextToSpeech.SUCCESS){
                    int result = textToSpeech.setLanguage(Locale.CHINA);
                    if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
                            && result != TextToSpeech.LANG_AVAILABLE){
//                        LogUtils.logger.trace("当前引擎不支持中文朗读");
                        Log.d(TAG, "createTTSCHINA: 当前引擎不支持中文朗读");
                    }
                }

            });


        }else {

            //生成默认引擎的speech
            textToSpeech = new TextToSpeech(context,status->{

                if(status==TextToSpeech.SUCCESS){
                    int result = textToSpeech.setLanguage(Locale.CHINA);
                    if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
                            && result != TextToSpeech.LANG_AVAILABLE){
//                        LogUtils.logger.trace("当前引擎不支持中文朗读");
                        Log.d(TAG, "createTTSCHINA: ");
                    }
                }

            },name);

        }
    }


    public void release(){

        if(textToSpeech!=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
            }
            return hexString.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


}
