package com.humber.its2020.ibourit.entity

data class Article (
    val id: Int = 0,
    val userId: Int,
    val content: String,
    val like: Int,
    val comments: List<String>
)