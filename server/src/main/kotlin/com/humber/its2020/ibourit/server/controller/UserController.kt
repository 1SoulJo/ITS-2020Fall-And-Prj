package com.humber.its2020.ibourit.server.controller

import com.humber.its2020.ibourit.server.entity.User
import com.humber.its2020.ibourit.server.repository.UserRepository
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class UserController (val repo: UserRepository) {
    @PostMapping("/user")
    fun addUser(@RequestBody user: User) {
        repo.save(user)
    }

    @GetMapping("/user/{id}")
    fun getUser(@PathVariable id: Long): Optional<User> {
        return repo.findById(id)
    }

    @GetMapping("/user")
    fun getAllUser(): List<User> {
        return repo.findAll().toList();
    }
}