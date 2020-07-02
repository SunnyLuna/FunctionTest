package com.decard.zj.founctiontest.network.base

import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

object RxExceptionUtil {

    fun exceptionHandler(throwable: Throwable): String {
        var errorMsg = "未知错误"
        if (throwable is UnknownHostException) {
            errorMsg = "网络不可用"
        } else if (throwable is SocketTimeoutException) {
            errorMsg = "请求网络超时"
        } else if (throwable is HttpException) {
            errorMsg = convertStatusCode(throwable)
        } else if (throwable is ParseException || throwable is JSONException) {
            errorMsg = "数据解析错误"
        }
        return errorMsg
    }

    fun convertStatusCode(httpException: HttpException): String {
        var msg = httpException.message()
        when {
            httpException.code() in 500..599 -> msg = "服务器处理请求出错"
            httpException.code() in 400..499 -> msg = "服务器无法处理请求"
            httpException.code() in 300..399 -> msg = "请求被重定向到其他页面"
        }
        return msg
    }
}