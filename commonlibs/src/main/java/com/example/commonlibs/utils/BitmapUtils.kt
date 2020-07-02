package com.example.commonlibs.utils


import android.graphics.*
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * Bitmap 工具类
 * @author ZJ
 * created at 2019/11/25 11:18
 */

object BitmapUtils {

    fun getBitmap(file: File?): Bitmap? {
        if (file == null) return null
        return BitmapFactory.decodeFile(file.absolutePath)
    }

    /**
     * 位图合成
     */
    @JvmStatic
    fun combineBitmap(background: Bitmap?, foreground: Bitmap): Bitmap? {
        if (background == null) {
            return null
        }
        val bgWidth = background.width
        val bgHeight = background.height
        val fgWidth = foreground.width
        val fgHeight = foreground.height

        val signatureBitmap = Bitmap.createScaledBitmap(
                foreground,
                fgWidth / 3,
                fgHeight / 3,
                true
        )
        val newmap = Bitmap
                .createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newmap)
        val paint = Paint()
        canvas.drawBitmap(background, Matrix(), null)
        canvas.drawBitmap(
                signatureBitmap,
                bgWidth - signatureBitmap.width - 10F,
                (bgHeight - signatureBitmap.height) - 10F,
                paint
        )
        canvas.save()
        canvas.restore()
        return newmap
    }

    /**
     * 字符串转bitmap
     *
     * @param bmpStr 字符串
     * @return bitmap
     */
    @JvmStatic
    fun stringToBitmap(bmpStr: String): Bitmap? {
        // OutputStream out;
        return try {
            val bitmapArray: ByteArray = HexUtils.hexStringToBytes(bmpStr)
            BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.size)
        } catch (e: Exception) {
            null
        }
    }

    /**
     *将string字符串转为bytes
     */
    @JvmStatic
    fun stringToBytes(str: String): ByteArray {
        val bitmap = stringToBitmap(str)
        val baos = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }

    /**
     *将bitmap转为bytes
     */
    @JvmStatic
    fun bitmapToBytes(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) return null
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }

    /**
     * Bytes to Bitmap
     */
    @JvmStatic
    fun bytesToBitmap(bmpStr: ByteArray): Bitmap? {
        return if (bmpStr.isEmpty()) {
            null
        } else {
            BitmapFactory.decodeByteArray(bmpStr, 0, bmpStr.size)
        }

    }

    /**
     * 按正方形剪裁图片
     * 指定正方形边长
     */
    @JvmStatic
    fun cropBitmap(bitmap: Bitmap, width: Int): Bitmap {
        var width = width
        // 得到图片的宽，高
        val w = bitmap.width
        val h = bitmap.height

        //width最大不能超过长方形的短边
        if (w < width || h < width) {
            width = if (w > h) h else w
        }

        val retX = (w - width) / 2
        val retY = (h - width) / 2

        return Bitmap.createBitmap(bitmap, retX, retY, width, width + 30, null, false)
    }

    /**
     * 从中间截取一个正方形
     */
    fun cropBitmap(bitmap: Bitmap): Bitmap {
        val w = bitmap.width // 得到图片的宽，高
        val h = bitmap.height
        val cropWidth = if (w >= h) h else w// 裁切后所取的正方形区域边长

        return Bitmap.createBitmap(bitmap, (bitmap.width - cropWidth) / 2,
                (bitmap.height - cropWidth) / 2, cropWidth, cropWidth)
    }

    /**
     * 把图片裁剪成圆形
     */
    fun getCircleBitmap(bitmap: Bitmap?): Bitmap? {//把图片裁剪成圆形
        var bitmap: Bitmap? = bitmap ?: return null
        bitmap = cropBitmap(bitmap!!)//裁剪成正方形
        try {
            val circleBitmap = Bitmap.createBitmap(bitmap.width,
                    bitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(circleBitmap)
            val paint = Paint()
            val rect = Rect(0, 0, bitmap.width,
                    bitmap.height)
            val rectF = RectF(Rect(0, 0, bitmap.width,
                    bitmap.height))
            var roundPx = 0.0f
            roundPx = bitmap.width.toFloat()
            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = Color.WHITE
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            val src = Rect(0, 0, bitmap.width,
                    bitmap.height)
            canvas.drawBitmap(bitmap, src, rect, paint)
            return circleBitmap
        } catch (e: Exception) {
            return bitmap
        }
    }
}