package com.decard.zj.founctiontest.serialport

import android.os.SystemClock
import com.decard.zj.kotlinbaseapplication.utils.RxBusInner
import java.io.BufferedInputStream
import java.io.InputStream

/**
 * File Description
 * @author Dell
 * @date 2018/9/28
 *
 */
class SerialReadThread(inputStream: InputStream) : Thread() {

    private var mInputStream: BufferedInputStream = BufferedInputStream(inputStream)

    override fun run() {
        val received = ByteArray(1024)
        var size = 0
        while (true) {
            if (Thread.currentThread().isInterrupted) {
                break
            }
            val available = mInputStream.available()
            if (available > 0) {
                size = mInputStream.read(received)
                if (size > 0) {
                    onDataReceive(received, size)
                }
            } else {
                SystemClock.sleep(1)
            }
        }
    }

    /**
     * 处理获取到的数据
     */
    private fun onDataReceive(received: ByteArray, size: Int) {
        val receiveStr = received.toString()
        RxBusInner.getInstance().post(receiveStr)
    }

    /**
     * 停止读线程
     */
    fun close(): Unit {
        mInputStream.close()
    }
}