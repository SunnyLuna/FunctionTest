package com.example.commonlibs.utils

import android.os.Environment
import android.os.StatFs
import android.util.Log
import com.example.commonlibs.BaseApplication
import java.io.File
import java.io.FileOutputStream


/**
 * sd卡相关的辅助类
 * @author ZJ
 * created at 2019/4/26 16:00
 */
object SDCardUtil {

    private val TAG = "------SDCardUtil"
    /**
     * 判断sd卡是否可用
     */
    fun isSDCardEnable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * 获取sd卡路径
     */
    fun getSDCardPath(): String {
        return Environment.getExternalStorageDirectory().absolutePath + File.separator
    }

    /**
     * 获取SD卡的剩余容量
     * 单位Mb
     */
    fun getRemainCapacity(): Long {
        if (isSDCardEnable()) {
            val statFs = StatFs(getSDCardPath())
            //获取空闲数据块的数量
            val availableBlocks = statFs.blockCountLong
            //获取单个数据块的大小
            val freeBlocks = statFs.blockSizeLong
            return availableBlocks * freeBlocks / 1024 / 1024
        }
        return -1
    }

    /**
     * 将assets里面的文件移动至sd卡根目录下
     */
    fun moveAssetsToSDCard(fileName: String) {
        val inputStream = BaseApplication.instance.assets.open(fileName)
        val filePath = getSDCardPath() + fileName
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        val outputStream = FileOutputStream(file)
        outputStream.write(inputStream.readBytes())
        outputStream.close()
        inputStream.close()
    }

    /**
     * 将raw里面的文件复制到sd卡根目录下
     * rawId 资源id
     * fileName 文件全名称
     */
    fun moveRawToSDCard(rawId: Int, fileName: String) {
        val inputStream = BaseApplication.instance.resources.openRawResource(rawId)
        val filePath = getSDCardPath() + fileName
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        val outputStream = FileOutputStream(file)
        outputStream.write(inputStream.readBytes())
        outputStream.close()
        inputStream.close()
    }

    /**
     *fileName 文件全名称
     * fileContent 文件内容
     */
    fun saveToSDCard(fileName: String, fileContent: String) {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val file = File(Environment.getExternalStorageDirectory(), fileName)
            if (file.exists()) {
                file.delete()
            }
            val outStream = FileOutputStream(file, true)
            outStream.write(fileContent.toByteArray())
            outStream.close()
            Log.d(TAG, "saveToSDCard: success")
        } else {
            Log.d(TAG, "saveToSDCard: error")
        }
    }
}