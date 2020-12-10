package com.humber.its2020.ibourit.web

import com.humber.its2020.ibourit.entity.Article
import retrofit2.Call
import retrofit2.http.*

interface ArticleApi {
    @GET("article")
    fun getArticles(): Call<List<Article>>

    @GET("article/address")
    fun getArticlesByAddress(
        @Query("city") city: String,
        @Query("state") state: String,
        @Query("country") country: String
    ): Call<List<Article>>

    @POST("article")
    fun uploadArticle(@Body article: Article): Call<Void>

    @DELETE("article/{id}")
    fun deleteArticle(@Path("id") id: Long): Call<Void>
}