package com.dgu.prompt.blaybus_backend.data.dto

import java.time.LocalDateTime

data class PostResponse(
    val postId: Int,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val viewCount: Int
)
