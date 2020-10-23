package com.humber.its2020.ibourit.server.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class User (
    @Id @GeneratedValue var id: Long,
    var email: String,
    var password: String,
    var fname: String,
    var lname: String
)