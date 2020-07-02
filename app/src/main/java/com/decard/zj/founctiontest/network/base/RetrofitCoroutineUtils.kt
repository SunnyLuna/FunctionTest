package com.decard.zj.founctiontest.network.base

import com.decard.zj.founctiontest.network.coroutine.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitCoroutineUtils {

    private fun create(url: String): Retrofit {
        val builder = OkHttpClient().newBuilder()
        builder.readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
        return Retrofit.Builder()
                .baseUrl(url)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
                .build()
    }

    private fun <T> getService(url: String, service: Class<T>): T {
        return create(url).create(service)
    }


    fun getUpload(): RetrofitService {
        return getService("https://api.douban.com/v2/", RetrofitService::class.java)
    }

}