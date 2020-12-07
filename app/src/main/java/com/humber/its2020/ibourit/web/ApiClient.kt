package com.humber.its2020.ibourit.web

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.humber.its2020.ibourit.entity.Article
import com.humber.its2020.ibourit.entity.User
import okhttp3.MultipartBody
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        private const val BASE_URL = "http://10.0.2.2:8080"
    }

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

//    fun uploadImage(userId: Long, category: Int, articleId: String, file: MultipartBody.Part,
    fun uploadImage(file: MultipartBody.Part,
                cb: Callback<Void>) {
//        imageApi().upload(userId, category, articleId, file).enqueue(cb)
        imageApi().upload(file).enqueue(cb)

    }

    fun getUsers(cb: Callback<List<User>>) {
        userApi().getUsers().enqueue(cb)
    }
}