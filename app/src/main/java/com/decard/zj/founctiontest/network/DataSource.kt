package com.decard.zj.founctiontest.network

import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DataSource {
    fun initAsync(idCardNumber: String, callback: LoadTasksCallback) {
        RetrofitUtil.getSocialNumber().getSocialNumber("370283198912164115").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<SocialBean> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: SocialBean) {
                        callback.onTasksLoaded(t)
                    }

                    override fun onError(e: Throwable) {
                        callback.onDataNotAvailable(e.message!!)
                    }
                })

    }

    fun getName(callback: LoadTasksCallback) {
        RetrofitUtil.getTest().getName().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<String> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(s: String) {
                callback.onGetName(s)
            }

            override fun onError(e: Throwable) {
                callback.onDataNotAvailable(e.message!!)
            }

            override fun onComplete() {

            }
        })
    }
}
