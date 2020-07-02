package com.decard.zj.founctiontest.serialport

import android.os.HandlerThread
import android.serialport.SerialPort
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.OutputStream

/**
 * File Description
 * @author Dell
 * @date 2018/9/28
 *
 */
class SerialPortManager {
    private var mReadThread: SerialReadThread? = null
    private var mOutPutStream: OutputStream? = null
    private var mWriteThread: HandlerThread? = null
    private var serialPort: SerialPort? = null

    companion object {
        fun getInstance() = Holder.instance
    }

    private object Holder {
        val instance = SerialPortManager
    }

    fun open(device: Device): SerialPort {
        return open(device.path, device.baudrate)
    }

    fun open(devicePath: String, baudrateStr: String): SerialPort {
        close()
        val device: File = File(devicePath)
        val baudrate: Int = baudrateStr.toInt()
        serialPort = SerialPort(device, baudrate, 0)
        mReadThread = SerialReadThread(serialPort!!.inputStream)
        mReadThread!!.start()
        mOutPutStream = serialPort!!.outputStream
        mWriteThread = HandlerThread("write")
        mWriteThread!!.start()
        return serialPort!!
    }

    fun close() {
        mReadThread!!.close()
        mOutPutStream!!.close()
        serialPort!!.close()
        serialPort = null
    }

    private fun sendData(datas: ByteArray): Observable<Any> {
        return Observable.create {
            mOutPutStream!!.write(datas)
        }
    }

    /**
     * 发送数据
     */
    fun sendCommand(command: String) {
        sendData(command.toByteArray()).subscribeOn(Schedulers.io()).subscribe { }
    }


}