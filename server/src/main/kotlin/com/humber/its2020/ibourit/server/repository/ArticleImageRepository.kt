package com.humber.its2020.ibourit.server.repository

import com.humber.its2020.ibourit.server.entity.Image
import org.springframework.data.repository.CrudRepository

interface ArticleImageRepository : CrudRepository<Image, Long>