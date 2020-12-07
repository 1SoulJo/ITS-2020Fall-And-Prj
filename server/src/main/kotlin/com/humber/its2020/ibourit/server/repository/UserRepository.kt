package com.humber.its2020.ibourit.server.repository

import com.humber.its2020.ibourit.server.entity.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Long>