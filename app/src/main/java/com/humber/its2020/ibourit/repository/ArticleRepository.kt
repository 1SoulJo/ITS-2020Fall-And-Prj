package com.humber.its2020.ibourit.repository

import androidx.lifecycle.MutableLiveData
import com.humber.its2020.ibourit.entity.Article

class ArticleRepository {
    var articles: MutableLiveData<Article> = MutableLiveData()
}