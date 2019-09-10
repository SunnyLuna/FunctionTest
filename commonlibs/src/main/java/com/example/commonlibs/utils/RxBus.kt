package com.example.commonlibs.utils


import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.ConcurrentHashMap

/**
 * File Description
 *
 * @author Dell
 * @date 2018/8/23
 */
class RxBus private constructor() {


    //Subject实现了Observer接口，所以有OnNext()方法。因为Subject继承了Observable，获取的时候可以转成Observable
    private val mSubject: Relay<Any>

    //防eventbus 发布粘性事件
    private val mSticky: MutableMap<Class<*>, Any>

    init {

        //Subject是个抽象类，它有四个实现。分别是:

        //PublishSubject：从哪里订阅就从哪里开始发送数据。

        //AsyncSubject：无论输入多少参数，永远只输出最后一个参数。

        //BehaviorSubject：发送离订阅最近的上一个值，没有上一个值的时候会发送默认值。

        //ReplaySubject：无论何时订阅，都会将所有历史订阅内容全部发出。

        mSubject = PublishRelay.create<Any>().toSerialized()
        mSticky = ConcurrentHashMap()
    }


    companion object {

        val instance: RxBus
            get() = Holder.instance
    }

    private object Holder {
         val instance = RxBus()
    }

    /**
     * 发送消息
     *
     * @param s
     */
    fun post(s: Any) {
        mSubject.accept(s)
    }

    /**
     * 订阅消息
     *
     * @param eventType
     * @param <T>
     * @return
    </T> */
    fun <T> toObservable(eventType: Class<T>): Observable<T> {
        return mSubject.ofType(eventType)//过滤操作符
    }

    /**
     * 判断是否有观察者
     */
    fun hasObervers(): Boolean {
        return mSubject.hasObservers()
    }

    /**
     * 解除普通订阅绑定
     *
     * @param disposable
     */
    fun rxBusUnbind(disposable: CompositeDisposable?) {
        if (null != disposable && disposable.isDisposed) {
            disposable.clear()
        }
    }

    /**********************************粘性事件相关 */
    /**
     * 发送粘性事件
     *
     * @param event
     */
    fun postSticky(event: Any) {
        synchronized(mSticky) {
            mSticky.put(event.javaClass, event)
        }
        post(event)
    }

    /**
     * 订阅粘性事件
     *
     * @param eventType
     * @param <T>
     * @return
    </T> */
    fun <T> tObservableSticky(eventType: Class<T>): Observable<T> {
        synchronized(mSticky) {
            val observable = mSubject.ofType(eventType)
            val `object` = mSticky[eventType]
            return if (`object` == null) {
                observable
            } else {
                observable.mergeWith(Observable.create { emitter -> emitter.onNext(eventType.cast(`object`)) })
            }
        }
    }

    /**
     * 移除指定的粘性事件
     *
     * @param type
     * @param <T>
     * @return
    </T> */
    fun <T> removeStickyEvent(type: Class<T>): T? {
        synchronized(mSticky) {
            return type.cast(mSticky.remove(type))
        }
    }

    /**
     * 移除所有的粘性事件
     */
    fun removeAllSticky() {
        mSticky.clear()
    }

}
