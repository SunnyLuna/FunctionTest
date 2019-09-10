package com.example.commonlibs.utils

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import com.example.commonlibs.BaseApplication
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Created by Horrarndoo on 2017/9/1.
 *
 *
 * 资源工具类-加载资源文件
 */

object ResourcesUtils {
    /**
     * 获取strings.xml资源文件字符串
     *
     * @param id 资源文件id
     * @return 资源文件对应字符串
     */
    fun getString(id: Int): String {
        return BaseApplication.getAppContext().resources.getString(id)
    }

    /**
     * 获取strings.xml资源文件字符串数组
     *
     * @param id 资源文件id
     * @return 资源文件对应字符串数组
     */
    fun getStringArray(id: Int): Array<String> {
        return BaseApplication.getAppContext().resources.getStringArray(id)
    }

    /**
     * 获取drawable资源文件图片
     *
     * @param id 资源文件id
     * @return 资源文件对应图片
     */
    fun getDrawable(id: Int): Drawable {
        return BaseApplication.getAppContext().resources.getDrawable(id)
    }

    /**
     * 获取colors.xml资源文件颜色
     *
     * @param id 资源文件id
     * @return 资源文件对应颜色值
     */
    fun getColor(id: Int): Int {
        return BaseApplication.getAppContext().resources.getColor(id)
    }

    /**
     * 获取颜色的状态选择器
     *
     * @param id 资源文件id
     * @return 资源文件对应颜色状态
     */
    fun getColorStateList(id: Int): ColorStateList {
        return BaseApplication.getAppContext().resources.getColorStateList(id)
    }

    /**
     * 获取dimens资源文件中具体像素值
     *
     * @param id 资源文件id
     * @return 资源文件对应像素值
     */
    fun getDimen(id: Int): Int {
        return BaseApplication.getAppContext().resources.getDimensionPixelSize(id)// 返回具体像素值
    }

    /**
     * 加载布局文件
     *
     * @param id 布局文件id
     * @return 布局view
     */
    fun inflate(id: Int): View {
        return View.inflate(BaseApplication.getAppContext(), id, null)
    }

    fun readRaw(id: Int): InputStream {
        return BaseApplication.getAppContext().resources.openRawResource(id)
    }

    /**
     *return the content of Raw
     */
    fun readRawString(id: Int): String {
        val inputStream = BaseApplication.getAppContext().resources.openRawResource(id)//id是我的文件名，这里应该根据具体文件更改
        return inputStream2String(inputStream)
    }


    fun readAssets(fileName: String): InputStream {
        return BaseApplication.getAppContext().assets.open(fileName)
    }

    /**
     * return the content of asserts
     */
    fun readAssetsString(fileName: String): String {
        val inputStream = BaseApplication.getAppContext().assets.open(fileName)
        return inputStream2String(inputStream)
    }


    private fun inputStream2String(inputStream: InputStream): String {
        val reader = InputStreamReader(inputStream)
        val stringBuffer = StringBuffer()
        val b = CharArray(1024)
        try {
            reader.use { input ->
                while (input.read(b) != -1) {
                    stringBuffer.append(b)
                }
            }
        } catch (e: IOException) {
            Log.e("ReadingFile", "IOException")
        }
        return stringBuffer.toString()
    }


}
