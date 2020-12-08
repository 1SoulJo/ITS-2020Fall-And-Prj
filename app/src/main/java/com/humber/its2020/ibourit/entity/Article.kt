package com.humber.its2020.ibourit.entity


data class Article(
    val articleId: String,
    val userId: String,
    val userName: String,
    val description: String,
    val like: Int,
    val comments: List<String>?,
    val date: String,
    val category: Int,
    val brand: String,
    val name: String,
    val price: Int,
    val images: List<String>?,
    val lat: Double,
    val lng: Double
)