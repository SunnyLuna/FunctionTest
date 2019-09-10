
import com.decard.zj.mykotlintest.calender.api.RetrofitService
import com.example.commonlibs.network.StringConverterFactory
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

    private fun createMap(url: String): Retrofit {
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

    private fun <T> getService(url: String, service: Class<T>): T {
        return create(url).create(service)
    }

    private fun <T> getMapService(url: String, service: Class<T>): T {
        return createMap(url).create(service)
    }

    fun getUpload(): RetrofitService {
        return getService("http://15.72.10.222:8080/easyn/", RetrofitService::class.java)
    }

    fun getSocialNumber(): RetrofitService {
        return getService("http://15.72.10.69:8090/", RetrofitService::class.java)
    }

    fun getSocialMap(): RetrofitService {
        return getMapService("http://15.72.10.69:8090/", RetrofitService::class.java)
    }
}