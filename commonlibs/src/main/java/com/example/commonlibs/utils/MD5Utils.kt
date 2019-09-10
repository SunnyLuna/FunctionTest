package com.example.commonlibs.utils

import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 *
 * @author ZJ
 * created at 2019/8/14 9:01
 */

object MD5Utils {

    fun getFileMD5(file: File?): String {
        if (!file?.isFile!!) {
            return ""
        }
        val digest: MessageDigest?
        val input: FileInputStream?
        val buffer = ByteArray(1024)
        var len: Int
        try {
            digest = MessageDigest.getInstance("MD5")
            input = FileInputStream(file)
            while (true) {
                len = input.read(buffer)
                if (len == -1) {
                    break
                }
                digest!!.update(buffer, 0, len)
            }
            input.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }

        return bytesToHexString(digest!!.digest())
    }


    fun getMD5(input: String): String {
        return try {
            val messageDigest = MessageDigest.getInstance("MD5")
            val inputByteArray = input.toByteArray()
            messageDigest.update(inputByteArray)
            val resultByteArray = messageDigest.digest()
            bytesToHexString(resultByteArray)
        } catch (e: NoSuchAlgorithmException) {
            ""
        }
    }

    private fun bytesToHexString(src: ByteArray?): String {
        val stringBuilder = StringBuilder("")
        if (src == null || src.isEmpty()) {
            return ""
        }
        for (i in src.indices) {
            val v = src[i].toInt() and 0xFF
            val hv = Integer.toHexString(v)
            if (hv.length < 2) {
                stringBuilder.append(0)
            }
            stringBuilder.append(hv)
        }
        return stringBuilder.toString()
    }
}
