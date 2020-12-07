package com.humber.its2020.ibourit.server.entity

import javax.persistence.*

@Entity
class Image (
        @Id @GeneratedValue var id: Long,

        var type: Int,

        var articleId: String,

        @Column(name = "picByte", length = 1000)
        val picByte: ByteArray
)