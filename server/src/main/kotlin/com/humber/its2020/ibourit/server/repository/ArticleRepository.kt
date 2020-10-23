package com.humber.its2020.ibourit.server.repository

import com.humber.its2020.ibourit.server.entity.Article
import org.springframework.data.repository.CrudRepository

interface ArticleRepository : CrudRepository<Article, Long> {

}