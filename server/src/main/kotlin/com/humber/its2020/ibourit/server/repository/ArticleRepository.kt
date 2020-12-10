package com.humber.its2020.ibourit.server.repository

import com.humber.its2020.ibourit.server.entity.Article
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface ArticleRepository : CrudRepository<Article, Long> {
    @Query("SELECT a FROM Article a WHERE a.articleId=:id")
    fun findByArticleId(@Param("id") articleId: String): Article

    @Query("SELECT a FROM Article a ORDER by a.date DESC")
    fun findAllArticles(): List<Article>

    @Query("SELECT a FROM Article a WHERE a.city=:city AND a.state=:state AND a.country=:country")
    fun findByAddress(
        @Param("city") city: String,
        @Param("state") state: String,
        @Param("country") country: String): List<Article>
}