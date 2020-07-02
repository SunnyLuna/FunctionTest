package com.decard.zj.founctiontest.network.base

import android.accounts.NetworkErrorException
import android.util.Log
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException


abstract class BaseObserver<T> : Observer<BaseResponse<T>> {

    fun BaseObserver() {

    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(tBaseResponse: BaseResponse<T>) {
        Log.d("------- success: ", tBaseResponse.toString())
        if (tBaseResponse.status == 1) {
            onSuccees(tBaseResponse.result)
        } else {
            onFailure(Throwable(tBaseResponse.msg), false)
        }
    }

    override fun onError(e: Throwable) {
        Log.d("--------  error: ", e.message)
        if (e is ConnectException
                || e is TimeoutException
                || e is NetworkErrorException
                || e is UnknownHostException
        ) {
            onFailure(e, true)
        } else {
            onFailure(e, false)
        }

    }

    override fun onComplete() {

    }

    /**
     * 返回成功
     *
     * @param t
     * @throws Exception
     */
    @Throws(Exception::class)
    protected abstract fun onSuccees(t: T)

    /**
     * 返回成功了,但是code错误
     *
     * @param t
     * @throws Exception
     */
    @Throws(Exception::class)
    protected fun onCodeError(t: BaseResponse<T>) {
    }

    /**
     * 返回失败
     *
     * @param e
     * @param isNetWorkError 是否是网络错误
     * @throws Exception
     */
    @Throws(Exception::class)
    protected abstract fun onFailure(e: Throwable, isNetWorkError: Boolean)


}
