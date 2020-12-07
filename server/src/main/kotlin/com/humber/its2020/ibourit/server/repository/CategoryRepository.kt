package com.humber.its2020.ibourit.server.repository

import com.humber.its2020.ibourit.server.entity.Category
import org.springframework.data.repository.CrudRepository

interface CategoryRepository : CrudRepository<Category, Long>