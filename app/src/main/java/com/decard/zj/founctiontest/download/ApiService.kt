package com.decard.zj.founctiontest.download

import com.decard.appstore.net.responsebean.InitResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * @Author: liuwei
 *
 * @Create: 2019/5/24 9:55
 *
 * @Description:

 */
interface ApiService {
    /**
     * 初始化接口，传入设备类型、设备SN号、设备经纬度信息，获取postmark，相当于token，之后每一步都需要该值
     */
    @Headers("Content-Type: application/json")
    @POST("init/init")
    fun getPostMark(@Body body: RequestBody): Observable<BaseResponse<InitResponse>>


    /**
     * 主页获取应用列表
     */
    @GET("software/list")
    fun getAllApp(@Header("postmark") postmark: String): Observable<BaseResponse<List<AllAppInfoResponse>>>

    /**
     * 文件下载接口
     */
    @Streaming
    @GET("ops/download")
    fun fileDownload(
            @Header("postmark") postmark: String,
            @Header("RANGE") start: String,
            @Query("software_id") softwareId: String,
            @Query("version") version: String
    ): Observable<Response<ResponseBody>>

}
