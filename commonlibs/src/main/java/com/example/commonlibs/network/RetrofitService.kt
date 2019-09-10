package com.decard.zj.mykotlintest.calender.api

import com.example.commonlibs.network.DataBean
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/* @FormUrlEncoded    表示请求体是一个form表单   多用在登录页面    Content-Type:application/x-www-form-urlencoded

       @Multipart 表示请求体是一个支持文件上传的form表单   多用在文件上传的网页    Content-Type:multipart/form-data

       @Streaming   表示相应体的数据以流的形式返回，
                    如果没有使用该注解，默认会把数据全部载入内存，之后通过流获取数据也不过是读取内存中的数据
                    一般当返回的数据比较大时，使用该注解*/

/*  @Field和 @FieldMap 与FormUrlEncoded配合
             @FieldMap 的接收类型是Map<String,String>,非String类型会调用其ToString方法

    @Part 和 @PartMap  与 Multipart配合 适合有文件上传的情况
             @PartMap 的默认接收类型是Map<String,RequestBody>,非RequestBody对象会通过Converter转换*/
interface RetrofitService {


    /**
     * GET 请求
     * 不带参数
     */
    @GET("hello")
    fun getName(): Observable<String>

    /**
     * get请求
     * 带参
     */
    @GET("qdxcjd/d3/f91002/getAAC001.action")
    fun getSocialNumber(@Query("AAC147") idCardNumber: String): Observable<String>


    /**
     * post请求
     */
    @Headers("Content-Type: application/json", "Accept: application/json")//需要添加头
    @POST("cateringData/add")
    fun post(@Body info: RequestBody): Observable<String> //使用@body可以把参数放到请求体中，适用于POST/PUT请求


    @FormUrlEncoded//
    @POST("CCBIS/B2CMainPlat_00_ENPAY")
    fun payForm(@Field("MERCHANTID") merchantId: String,
                @Field("BRANCHID") branchId: String,
                @Field("POSID") posId: String,
                @Field("ccbParam") ccbParam: String): Observable<String>

    @FormUrlEncoded
    @POST("CCBIS/B2CMainPlat_00_ENPAY")
    fun payFormPlus(@FieldMap stringMap: Map<String, String>): Observable<String>

    /**
     * post 上传文件
     */
    @Multipart
    @POST("arvserv/archBase64File.do")
    fun upload(@PartMap map: MutableMap<String, RequestBody>,
               @Part file: MultipartBody.Part): Observable<String>


    @GET("api/4/news/latest")
    fun getData(): Observable<DataBean>
}