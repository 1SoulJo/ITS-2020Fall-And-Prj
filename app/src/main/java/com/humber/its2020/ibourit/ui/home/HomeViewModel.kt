package com.humber.its2020.ibourit.ui.home

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.humber.its2020.ibourit.entity.Article
import com.humber.its2020.ibourit.web.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val articles = MutableLiveData<List<Article>>()

    val adapter: ArticleAdapter = ArticleAdapter()

    init {
        loadArticles()
    }

    fun getArticles(): LiveData<List<Article>> {
        return articles
    }

    private fun loadArticles() {
        ApiClient.getArticles(object: Callback<List<Article>> {
            override fun onResponse(call: Call<List<Article>>, response: Response<List<Article>>) {
                if (response.body().isNullOrEmpty()) {
                    return
                }
                articles.value = response.body()!!
            }

            override fun onFailure(call: Call<List<Article>>, t: Throwable) {
                Log.d("HomeViewModel", t.message!!)
            }
        })
    }
}