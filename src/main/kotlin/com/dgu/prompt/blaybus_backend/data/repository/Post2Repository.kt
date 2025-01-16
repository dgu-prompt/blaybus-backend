package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.Post2
import org.springframework.data.jpa.repository.JpaRepository

interface Post2Repository : JpaRepository<Post2, Int> {
    fun findByPostId(postId: Int): Post2?
}
