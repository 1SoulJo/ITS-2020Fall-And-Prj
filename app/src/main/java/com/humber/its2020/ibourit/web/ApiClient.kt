package com.humber.its2020.ibourit.web

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.humber.its2020.ibourit.constants.Urls.Companion.BASE_URL
import com.humber.its2020.ibourit.entity.Article
import com.humber.its2020.ibourit.entity.User
import okhttp3.MultipartBody
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    private val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private fun articleApi(): ArticleApi {
        return retrofit.create(ArticleApi::class.java)
    }

    private fun imageApi(): ImageApi {
        return retrofit.create(ImageApi::class.java)
    }

    private fun userApi(): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    fun getArticles(cb: Callback<List<Article>>) {
        articleApi().getArticles().enqueue(cb)
    }

    fun uploadArticle(article: Article, cb: Callback<Void>) {
        articleApi().uploadArticle(article).enqueue(cb)
    }

    fun uploadImage(userId: String, category: Int, articleId: String, file: MultipartBody.Part,
                    order: Int, cb: Callback<Void>) {
        imageApi().upload(userId, category, articleId, file, order).enqueue(cb)
    }

    fun getUsers(cb: Callback<List<User>>) {
        userApi().getUsers().enqueue(cb)
    }
}