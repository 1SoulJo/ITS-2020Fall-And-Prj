package com.humber.its2020.ibourit.server.controller

import com.humber.its2020.ibourit.server.entity.Category
import com.humber.its2020.ibourit.server.repository.CategoryRepository
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class CategoryController (var repo: CategoryRepository) {
    @PostMapping("/category")
    fun addCategory(@RequestBody category: Category) {
        repo.save(category)
    }

    @GetMapping("/category")
    fun getAllCategory(): List<Category> {
        return repo.findAll().toList();
    }
}