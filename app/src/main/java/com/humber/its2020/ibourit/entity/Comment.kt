package com.humber.its2020.ibourit.entity

data class Comment (
    val id: Long = 0,
    val userId: Long,
    val content: String
)