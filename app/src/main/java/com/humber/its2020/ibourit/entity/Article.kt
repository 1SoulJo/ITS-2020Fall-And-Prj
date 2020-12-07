package com.humber.its2020.ibourit.entity

data class Article(
    val id: String,
    val userId: Long,
    val itemInfo: ItemInfo?,
    val description: String,
    val like: Int,
    val comments: List<String>
)