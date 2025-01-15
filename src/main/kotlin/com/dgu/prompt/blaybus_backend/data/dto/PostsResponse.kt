package com.dgu.prompt.blaybus_backend.data.dto

import java.time.LocalDateTime

data class PostsResponse(
    val postId: Int,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
)

data class PostsPageResponse(
    val posts: List<PostsResponse>,
    val pagination: PaginationResponse
)

data class PaginationResponse(
    val currentPage: Int,
    val totalPages: Int,
    val totalPosts: Int
)
