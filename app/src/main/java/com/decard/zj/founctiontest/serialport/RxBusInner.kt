package com.decard.zj.founctiontest.serialport

import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

/**
 * File Description
 * @author Dell
 * @date 2018/9/12
 *
 */
class RxBusInner private constructor() {


    private val mSubject: Relay<Any> = PublishRelay.create()

    private val mSticky: MutableMap<Class<*>, Any> = mutableMapOf()

    companion object {
        fun getInstance() = Holder.rxBus
    }
    
    private object Holder{
        val rxBus = RxBusInner()
    }

    /**
     * 发送消息
     */
    fun post(s: Any) {
        mSubject.accept(s)
    }

    /**
     * 订阅消息
     */
    fun <T> toObservable(eventType: Class<T>): Observable<T> {
        return mSubject.ofType(eventType)
    }

    /**
     * 判断是否有订阅
     */
    fun haveObservers(): Boolean {
        return mSubject.hasObservers()
    }

    /**
     * 解除订阅
     */
    fun rxBusUnbind(disposable: CompositeDisposable) {
        if (disposable.isDisposed) {
            disposable.clear()
        }
    }

    /******************************************粘性事件相关*****************************************/

    /**
     * 发送粘性事件
     */
    fun postSticky(event: Any) {
        mSticky.put(event.javaClass, event)
        post(event)
    }

    /**
     * 订阅粘性事件
     */
    fun <T> toObservableSticky(eventType: Class<T>): Observable<T> {
        val observable: Observable<T> = mSubject.ofType(eventType)
        val any: Any? = mSticky[eventType]
        if (any == null) {
            return observable
        } else {
            return observable.mergeWith(Observable.create { emitter -> emitter.onNext(eventType.cast(any)) })
        }
    }

    /**
     * 解除粘性订阅
     */
    fun <T> rxBusUnbindSticky(type: Class<T>): T {
        return type.cast(mSticky.remove(type))
    }

    /**
     * 移除所有的粘性事件
     */
    fun removeAllSticky() {
        mSticky.clear()
    }
}