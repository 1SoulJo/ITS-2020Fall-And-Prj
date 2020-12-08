package com.humber.its2020.ibourit.server.entity

import javax.persistence.*

@Entity
class Image (
        @Id @GeneratedValue var id: Long = 0,
        var userId: String,
        var category: Int,
        var articleId: String,
        var path: String,
        var imageOrder: Int
)