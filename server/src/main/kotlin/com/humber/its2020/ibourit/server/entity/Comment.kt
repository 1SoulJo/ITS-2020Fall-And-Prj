package com.humber.its2020.ibourit.server.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Comment (
    @Id @GeneratedValue var id: Long,

    var articleId: String
)