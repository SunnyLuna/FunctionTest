package com.example.commonlibs.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;


@SuppressLint("UseValueOf")
public class ByteUtil {

    private static final String TAG = "-----ByteUtil";

    /**
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    private static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        int len = hexString.length();
        if (len % 2 != 0) {
            hexString = "0" + hexString;
            len = len + 1;
        }
        char[] hexBytes = hexString.toUpperCase().toCharArray();
        byte[] ret = new byte[len >> 1];
        for (int i = 0; i < len; i += 2) {
            ret[i >> 1] = (byte) (hexToDec(hexBytes[i]) << 4 | hexToDec(hexBytes[i + 1]));
        }
        return ret;
    }

    /**
     * hexChar转int
     *
     * @param hexChar hex单个字节
     * @return 0..15
     */
    private static int hexToDec(char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - '0';
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

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
            bitmapArray = ByteUtil.hexStringToBytes(bmpStr);
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

}