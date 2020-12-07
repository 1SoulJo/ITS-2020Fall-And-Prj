package com.humber.its2020.ibourit.web

import com.google.gson.GsonBuilder
import com.humber.its2020.ibourit.entity.Article
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        private const val BASE_URL = "https://localhost:8080"
    }

    private fun setupApi(): ArticleApi {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(ArticleApi::class.java)
    }

    fun getArticles(cb: Callback<List<Article>>) {
        setupApi().getArticles().enqueue(cb)
    }
}