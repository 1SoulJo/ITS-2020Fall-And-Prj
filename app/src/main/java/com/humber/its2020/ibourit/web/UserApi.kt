package com.humber.its2020.ibourit.web

import com.humber.its2020.ibourit.entity.User
import retrofit2.Call
import retrofit2.http.GET

interface UserApi {
    @GET("user")
    fun getUsers(): Call<List<User>>
}