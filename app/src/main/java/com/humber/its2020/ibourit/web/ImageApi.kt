package com.humber.its2020.ibourit.web

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageApi {
    @Multipart
    @POST("image")
    fun upload(
        @Part("userId") userId: String,
        @Part("category") category: Int,
        @Part("articleId") articleId: String,
        @Part file: MultipartBody.Part): Call<Void>
}