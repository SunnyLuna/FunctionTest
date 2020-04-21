package com.decard.zj.founctiontest.network

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * File Description
 * @author Dell
 * @date 2018/9/11
 *
 */
interface RetrofitService {


    @Multipart
    @POST("arvserv/archBase64File.do")
    fun upload(@PartMap map: MutableMap<String, RequestBody>,
               @Part file: MultipartBody.Part): Observable<ReturnBean>


    @GET("qdxcjd/d3/f91002/getAAC001.action")
    fun getSocialNumber(@Query("AAC147") idCardNumber: String): Observable<SocialBean>

    @GET("qdxcjd/d3/f91002/getAAC100.action")
    fun getSocialPhoto(@Query("AAC001") socialNumber: String): Observable<String>


    @GET("hello")
    fun getName(): Observable<String>

    @Multipart
    @POST("dc/intelligent")
    fun getDc(@Part("param") param: String): Observable<String>


}