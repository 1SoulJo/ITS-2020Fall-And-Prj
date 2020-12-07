package com.humber.its2020.ibourit.server.repository

import com.humber.its2020.ibourit.server.entity.Comment
import org.springframework.data.repository.CrudRepository

interface CommentRepository : CrudRepository<Comment, Long>