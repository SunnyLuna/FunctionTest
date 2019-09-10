package com.decard.zj.founctiontest.download

data class DownloadBean (val apkName:String, val progress:Int, val status:String){
    override fun toString(): String {
        return "DownloadBean(apkName='$apkName', progress=$progress, status='$status')"
    }
}