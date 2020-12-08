package com.humber.its2020.ibourit.server.entity

import java.util.*
import javax.persistence.*

@Entity
class Article (
        @Id @GeneratedValue var id: Long,
        var articleId: String,
        var description: String,
        var userId: String,
        var userName: String,
        var date: Date,
        var category: Int,
        var brand: String,
        var name: String,
        var price: Int,
        @ElementCollection
        val images: List<String>
)