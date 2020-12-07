package com.humber.its2020.ibourit.server.entity

import java.util.*
import javax.persistence.*

@Entity
class Article (
        @Id var id: String,

        var content: String,

        var userId: Long,

        var date: Date
)