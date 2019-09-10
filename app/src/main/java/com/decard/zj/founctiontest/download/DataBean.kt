package com.decard.zj.founctiontest.download


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

/**
 * @Author: liuwei
 *
 * @Create: 2019/5/20 14:38
 *
 * @Description:

 */

open class DataBean : RealmObject(), Serializable {

    @PrimaryKey
    open var taskId = "" // taskId = softwareId+apkVersion
    open var fileMD5 = ""
    open var softwareId: String = ""
    open var imageUrl: String? = null
    open var apkName: String? = null
    open var apkInfo: String? = null
    open var apkVersion: String? = null
    open var apkSize: String? = null
    open var progress: Int = -1
    open var status = "下载"
    open var position = -1

    open var filePath: String? = null
    open var packetName: String? = null

    open var fileSize: Long = 0
    open var downloadSize: Long = 0
    open var timeStamp = ""
    override fun toString(): String {
        return "DataBean(taskId='$taskId', fileMD5='$fileMD5', softwareId='$softwareId', imageUrl=$imageUrl, apkName=$apkName, apkInfo=$apkInfo, apkVersion=$apkVersion, apkSize=$apkSize, progress=$progress, status='$status', position=$position, filePath=$filePath, packetName=$packetName, fileSize=$fileSize, downloadSize=$downloadSize, timeStamp='$timeStamp')"
    }


}