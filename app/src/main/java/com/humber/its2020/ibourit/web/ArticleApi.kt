package com.humber.its2020.ibourit.web

import com.humber.its2020.ibourit.entity.Article
import retrofit2.Call
import retrofit2.http.GET

interface ArticleApi {
    @GET("article")
    fun getArticles(): Call<List<Article>>
}