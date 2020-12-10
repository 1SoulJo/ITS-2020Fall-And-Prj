package com.humber.its2020.ibourit.server.controller

import com.humber.its2020.ibourit.server.entity.Article
import com.humber.its2020.ibourit.server.repository.ArticleRepository
import org.springframework.web.bind.annotation.*


@RestController
class ArticleController (val repo: ArticleRepository) {
    @PostMapping("/article")
    fun addPost(@RequestBody article: Article) {
        repo.save(article)
    }

    @GetMapping("/article")
    fun getAllPost(): List<Article> {
        return repo.findAllArticles().toList()
    }

    @DeleteMapping("/article/{id}")
    fun deletePost(@PathVariable id: Long) {
        repo.deleteById(id)
    }
}