package com.example.commonlibs.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;


@SuppressLint("UseValueOf")
public class ByteUtil {

    private static final String TAG = "-----ByteUtil";


    /**
     * 字符串转bitmap
     *
     * @param bmpStr 字符串
     * @return bitmap
     */
    public static Bitmap convertStringToIcon(String bmpStr) {
        // OutputStream out;
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = HexUtils.hexStringToBytes(bmpStr);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /*
     *将bitmap转为bytes
     */
    public static byte[] bitmapToBytes(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] photos = baos.toByteArray();
        return photos;
    }


    /*
     *将string字符串转为bytes
     */
    public static byte[] stringToBytes(String str) {
        Bitmap bitmap = convertStringToIcon(str);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] photos = baos.toByteArray();
        return photos;
    }

    /**
     * 按正方形剪裁图片
     * 指定正方形边长
     */
    public static Bitmap imageCrop(Bitmap bitmap, int width) {
        // 得到图片的宽，高
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        //width最大不能超过长方形的短边
        if (w < width || h < width) {
            width = w > h ? h : w;
        }

        int retX = (w - width) / 2;
        int retY = (h - width) / 2;

        return Bitmap.createBitmap(bitmap, retX, retY, width, width + 30, null, false);
    }

    public static Bitmap convertbytesToIcon(byte[] bmpStr) {

        try {
            return BitmapFactory.decodeByteArray(bmpStr, 0,
                    bmpStr.length);
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] concat(byte[] first, byte[] second) {
        if (first == null) {
            return second;
        }
        if (second == null) {
            return first;
        }
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

}