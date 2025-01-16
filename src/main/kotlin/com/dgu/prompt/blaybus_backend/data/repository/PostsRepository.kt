package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PostsRepository : JpaRepository<Post, Int> {
    @Query("SELECT COUNT(p) FROM Post p")
    fun countTotalPosts(): Long

    @Query("SELECT p FROM Post p ORDER BY p.postId ASC")
    fun findAllPostsOrdered(): List<Post>
}
