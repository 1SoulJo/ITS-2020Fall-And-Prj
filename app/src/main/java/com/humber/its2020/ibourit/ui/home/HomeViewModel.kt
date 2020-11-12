package com.humber.its2020.ibourit.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.humber.its2020.ibourit.entity.Article

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
        val list = ArrayList<Article>()
        list.add(Article(0, 0, "hi", 100, listOf("c1", "c2")))
        list.add(Article(0, 0, "hi", 100, listOf("c1", "c2")))
        list.add(Article(0, 0, "hi", 100, listOf("c1", "c2")))
        list.add(Article(0, 0, "hi", 100, listOf("c1", "c2")))
        list.add(Article(0, 0, "hi", 100, listOf("c1", "c2")))
        articles.value = list
    }
}