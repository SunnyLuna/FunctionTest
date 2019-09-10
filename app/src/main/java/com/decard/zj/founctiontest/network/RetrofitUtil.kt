package com.decard.zj.founctiontest.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * File Description
 * @author Dell
 * @date 2018/9/11
 *
 */
object RetrofitUtil {

    //companion object声明static变量
    /**
     * 创建retrofit
     */
    private fun create(url: String): Retrofit {
        val builder = OkHttpClient().newBuilder()
        builder.readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
        return Retrofit.Builder()
                .baseUrl(url)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())

                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }
    private fun <T> getService(url: String, service: Class<T>): T {
        return create(url).create(service)
    }



    private fun createString(url: String): Retrofit {
        val builder = OkHttpClient().newBuilder()
        builder.readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
        return Retrofit.Builder()
                .baseUrl(url)
                .client(builder.build())
                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }



    private fun <T> getStringService(url: String, service: Class<T>): T {
        return createString(url).create(service)
    }

    fun getUpload(): RetrofitService {
        return getService("http://15.72.10.222:8080/easyn/", RetrofitService::class.java)
    }

    fun getSocialNumber(): RetrofitService {
        return getService("http://15.72.10.69:8090/", RetrofitService::class.java)
    }

    fun getSocialMap(): RetrofitService {
        return getStringService("http://15.72.10.69:8090/", RetrofitService::class.java)
    }

    private fun createTest(url: String): Retrofit {
        val builder = OkHttpClient().newBuilder()
        builder.readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
        return Retrofit.Builder()
                .baseUrl(url)
                .client(builder.build())
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    private fun <T> getTestService(url: String, service: Class<T>): T {
        return createTest(url).create(service)
    }

    fun getTest(): RetrofitService {
        return getTestService("http://192.168.5.141:8080/", RetrofitService::class.java)
    }
}