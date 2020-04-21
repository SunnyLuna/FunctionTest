package com.decard.zj.founctiontest.download

import android.util.Log
import com.decard.zj.founctiontest.TestApplication
import com.decard.zj.kotlinbaseapplication.utils.RxBusInner
import com.example.commonlibs.apkinstaller.PackageUtils
import java.io.File
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 *
 * @author ZJ
 * created at 2019/8/2 11:26
 */
object DownloadManager {

    // 任务列表
    val jobList = java.util.HashMap<String, DownloadTask>()

    var pool: ThreadPoolExecutor? = null

    init {
        pool = ThreadPoolExecutor(
                5, 5, 30, TimeUnit.SECONDS,
                ArrayBlockingQueue(2000)
        )
    }


    /**
     * 添加下载任务
     */
    fun addTask(dataBean: DataBean) {

        //如果下载状态为等待下载、暂停、更新时，开始下载任务
        if (dataBean.status == DownloadStatus.DOWNLOAD
                || dataBean.status == DownloadStatus.PAUSE
                || dataBean.status == DownloadStatus.UPGRADE
        ) {
            if (getAttachmentState(dataBean) == 0) {
                val dbBeans = DBUtil.queryByTaskId(dataBean.taskId)

                if (dbBeans != null) {
                    RxBusInner.getInstance().post(DownloadBean(dbBeans.apkName!!, dbBeans.progress, DownloadStatus.DOWNLOADING))
                    startTask(dbBeans)
                } else {
                    RxBusInner.getInstance().post(DownloadBean(dataBean.apkName!!, dataBean.progress, DownloadStatus.DOWNLOADING))
                    startTask(dataBean)
                }
            }
            dataBean.status = DownloadStatus.DOWNLOADING
            //如果状态为正在下载,暂停下载
        } else if (dataBean.status == DownloadStatus.DOWNLOADING) {
            stopTask(dataBean)
            dataBean.status = DownloadStatus.PAUSE
            RxBusInner.getInstance().post(DownloadBean(dataBean.apkName!!, dataBean.progress, DownloadStatus.PAUSE))
            //状态为安装，安装app
        } else if (dataBean.status == DownloadStatus.INSTALL) {
            installApp(dataBean)
            //状态为打开，打开app
        } else if (dataBean.status == DownloadStatus.OPEN) {
            PacketsUtil.openApp(TestApplication.instance, dataBean.packetName)
        }
    }

    /**
     * 安装应用
     */
    private fun installApp(dataBean: DataBean) {

        val file = File(dataBean.filePath)
        val fileMD5 = MD5Util.getFileMD5(file)
        if (fileMD5 == dataBean.fileMD5) {
            val code = PackageUtils.install(TestApplication.instance, dataBean.filePath, dataBean.packetName)
            dataBean.status = DownloadStatus.OPEN
            RxBusInner.getInstance().post(DownloadBean(dataBean.apkName!!, -1, DownloadStatus.OPEN))
            DBUtil.addData(dataBean)
            if (code == 1) {
            } else {
                Log.d("---------", "应用安装失败")
            }
        } else {
            DBUtil.deleteByTaskId(dataBean.taskId)
            Log.d("---------", "MD5校验错误")
        }
    }

    /**
     * 开始下载
     */
    private fun startTask(dataBean: DataBean) {
        val downloadTask = DownloadTask(dataBean, pool!!)
        downloadTask.start()
        jobList[dataBean.taskId] = downloadTask
    }


    /**
     * 判断线程池中是否有当前任务
     */
    private fun getAttachmentState(dataBean: DataBean): Int {
        var exist = false
        jobList.forEach { (taskId, Task) ->
            if (taskId == dataBean.taskId) {
                exist = true
            }
        }
        if (exist) {
            return -1
        }
        return 0
    }

    /**
     * 停止下载
     */
    fun stopTask(data: DataBean) {
        val job = jobList[data.taskId]
        Log.d("------stopTask", data.apkName)
        job?.stop()
        jobList.remove(data.taskId)
    }

    /**
     * 停止所有的任务
     */
    fun stopAllTask() {
        jobList.forEach { (taskId, downloadTask) ->
            Log.d("------downTask", taskId)
            downloadTask.stop()
        }
    }
}