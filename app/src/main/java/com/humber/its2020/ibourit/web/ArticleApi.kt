package com.humber.its2020.ibourit.web

import com.humber.its2020.ibourit.entity.Article
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ArticleApi {
    @GET("article")
    fun getArticles(): Call<List<Article>>

    @POST("article")
    fun uploadArticle(@Body article: Article): Call<Void>
}