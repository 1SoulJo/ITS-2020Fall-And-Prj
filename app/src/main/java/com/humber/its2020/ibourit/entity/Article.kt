package com.humber.its2020.ibourit.entity

data class Article (
    val id: Long = 0,
    val userId: Long,
    val content: String,
    val like: Int,
    val comments: List<String>
)