package com.example.commonlibs.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtils {

    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Toast toast;

    public static void toast(final Context context, final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                } else {
                    toast.setText(msg);
                    toast.setDuration(Toast.LENGTH_SHORT);
                }
                toast.show();
            }
        });
    }

    public static void cancelToast() {
        if (toast != null)
            toast.cancel();
    }

    public static void toast(final Context context, final int resId) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
