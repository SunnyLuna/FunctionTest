package com.decard.zj.founctiontest.download

import android.util.Log
import com.decard.zj.founctiontest.serialport.RxBusInner
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.RandomAccessFile
import java.util.concurrent.ThreadPoolExecutor

class DownloadTask(val dataBean: DataBean, var threadPool: ThreadPoolExecutor) {

    var downLoadThread: DownLoadThread? = null

    fun start() {
        downLoadThread = DownLoadThread(dataBean)
        threadPool.execute(downLoadThread)
    }

    fun stop() {
        downLoadThread?.stopDownLoad()
        threadPool.remove(downLoadThread)
    }

    class DownLoadThread(val dataBean: DataBean) : Thread() {
        private var isDownloading = true
        override fun run() {
            var start = ""
            if (dataBean.status != DownloadStatus.DOWNLOAD) {
                start = "bytes=${dataBean.downloadSize}-"
            }
            RetrofitUtils.getData().fileDownload(DownloadActivity.postMark, start, dataBean.softwareId, dataBean.apkVersion!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(object : Observer<Response<ResponseBody>> {
                        override fun onNext(t: Response<ResponseBody>) {
                            write2Disk(dataBean, t)
                        }

                        override fun onComplete() {

                        }

                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onError(e: Throwable) {
                        }
                    })
        }

        fun write2Disk(dataBean: DataBean, response: Response<ResponseBody>) {
            val headerContent = response.headers().get("Content-Disposition")
            if (headerContent == null) {
                Log.d("----------", "服务器返回头为空")
                return
            }
            val apkNameContent = headerContent.split(";")[1]
            val apkName = apkNameContent.split("=")[1]
            dataBean.filePath = "/sdcard/appStore/download/$apkName"
            val file = File(dataBean.filePath)
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            if (!file.exists()) {
                file.createNewFile()
            }
            val randomAccessFile = RandomAccessFile(file, "rw")
            Log.e("TAG", "randomAccessFile:${randomAccessFile.length()} ${dataBean.downloadSize} ${dataBean.fileSize}")
            randomAccessFile.seek(dataBean.downloadSize)
            var currentLength: Long = dataBean.downloadSize
            val inStream = response.body()!!.byteStream() //获取下载输入流
            val totalLength = response.body()!!.contentLength()
            if (dataBean.fileSize == 0L) {
                dataBean.fileSize = totalLength
            }
            try {
                var len: Int
                val buffer = ByteArray(1024)
                while (isDownloading) {
                    len = inStream.read(buffer)
                    if (len == -1) {
                        break
                    }
                    randomAccessFile.write(buffer, 0, len)
                    currentLength += len
                    val progress = (100 * currentLength / dataBean.fileSize).toInt()
                    Log.d("--------=====", progress.toString() + "data:  " + dataBean.progress)
                    if (dataBean.progress != progress && dataBean.progress != 100) {
                        RxBusInner.getInstance().post(DownloadBean(dataBean.apkName!!, progress, DownloadStatus.DOWNLOADING))
                        dataBean.progress = progress
                        dataBean.downloadSize = currentLength
                    }
                    if ((100 * currentLength / dataBean.fileSize).toInt() == 100) {
                        dataBean.status = DownloadStatus.INSTALL
                        DownloadManager.stopTask(dataBean)
                        RxBusInner.getInstance().post(DownloadBean(dataBean.apkName!!, -1, DownloadStatus.INSTALL))
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } finally {
                if (dataBean.progress != 100 || dataBean.downloadSize != dataBean.fileSize) {
                    dataBean.status = DownloadStatus.PAUSE
                } else {
                    dataBean.progress = -1
                }
//                Log.d("-----------db", dataBean.toString())
                DBUtil.addData(dataBean)
                try {
                    randomAccessFile.close() //关闭输出流
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    inStream.close() //关闭输入流
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        fun stopDownLoad() {
            isDownloading = false
            if (dataBean.status == DownloadStatus.DOWNLOADING) {
                dataBean.status = DownloadStatus.PAUSE
            }
            DBUtil.addData(dataBean)
        }
    }
}


