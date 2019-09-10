package com.example.commonlibs.network

import android.util.Log
import com.decard.zj.mykotlintest.calender.api.RetrofitService
import com.example.commonlibs.BaseApplication
import com.example.commonlibs.utils.NetUtils
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


/**
 *
 * @author ZJ
 * created at 2019/4/24 20:23
 */
class RetrofitUtils {
    val CACHE_NAME = "你好帅"
    var BASE_URL = "http://news-at.zhihu.com/"
    private val DEFAULT_CONNECT_TIMEOUT = 30L
    private val DEFAULT_WRITE_TIMEOUT = 30L
    private val DEFAULT_READ_TIMEOUT = 30L
    private var httpService: RetrofitService
    private var retrofit: Retrofit
    private var okHttpClient: OkHttpClient

    private constructor() {
        //手动创建一个OkHttpClient并设置超时时间
        //设置缓存
        val cacheFile = File(BaseApplication.getAppContext().cacheDir, CACHE_NAME)
        val cache = Cache(cacheFile, 1024 * 1024 * 50)//设置缓存大小为50M
        val interceptor = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                var request = chain.request()
                if (!NetUtils.isConnected(BaseApplication.getAppContext())) {
                    request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
                }
                val response = chain.proceed(request)

                if (NetUtils.isConnected(BaseApplication.getAppContext())) {

                    val maxAge = 600//缓存失效时间
                    //有网络时，设置缓存超时时间
                    response.newBuilder()
                            .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .header("Cache-Control", "public, max-age=$maxAge")
                            .build()
                } else {
                    val maxAge = 600//缓存失效时间
                    response.newBuilder()
                            .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .header("Cache-Control", "only-if-cached, max-stale=$maxAge")//设置离线缓存时间
                            .build()
                }
                return response
            }
        }
        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.d("------", message) })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        okHttpClient = OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(interceptor)
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build()
        retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        httpService = retrofit.create(RetrofitService::class.java)

    }

    private object SingletonHolder {
        val INSTANCE = RetrofitUtils()
    }

    companion object {
        fun getInstance(): RetrofitUtils {
            return SingletonHolder.INSTANCE
        }

    }

    fun getHttpService(): RetrofitService {
        return httpService
    }

    fun changeBaseUrl(baseUrl: String) {
        retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build()
        httpService = retrofit.create(RetrofitService::class.java)
    }
}