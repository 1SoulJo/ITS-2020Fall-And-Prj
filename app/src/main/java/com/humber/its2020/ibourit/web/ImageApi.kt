package com.humber.its2020.ibourit.web

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ImageApi {
    @Multipart
    @POST("image")
    fun upload(
        @Query("userId") userId: String,
        @Query("category") category: Int,
        @Query("articleId") articleId: String,
        @Part file: MultipartBody.Part,
        @Query("imageOrder") order: Int): Call<Void>

    @GET("image")
    fun getImage(articleId: String): Call<List<String>>
}