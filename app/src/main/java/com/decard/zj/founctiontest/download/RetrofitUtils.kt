package com.decard.zj.founctiontest.download
import android.util.Log
import com.decard.zj.founctiontest.download.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLDecoder
import java.util.concurrent.TimeUnit

/**
 * File Description
 * @author Dell
 * @date 2018/9/11
 *
 */
object RetrofitUtils {

    //companion object声明static变量
    val TAG = "*------Retrofit"

    /**
     * 创建retrofit
     */
    private fun create(url: String): Retrofit {
        val builder = OkHttpClient().newBuilder()

        val paramInterceptor = Interceptor { chain ->
            val url = chain.request().url().url().toString()

            Log.d(TAG, "发送请求:${URLDecoder.decode(url, "utf-8")}")
            val response = chain.proceed(chain.request())
            //注意这里不能直接使用response.body.string(),否则流会关闭,会报异常
            val responseBody = response.peekBody(1024 * 1024)
            Log.d(TAG, "请求结果:${responseBody.string()}")
            return@Interceptor response
        }
        builder.readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(paramInterceptor)
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


    fun getData(): ApiService {
        return getService("http://47.105.35.37:19092/", ApiService::class.java)
    }

}