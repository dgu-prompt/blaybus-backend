package com.dgu.prompt.blaybus_backend.data.dto

data class PostRequest(
    val postTitle: String,
    val content: String
)

data class UpdatePostRequest(
    val postTitle: String?,
    val content: String?
)

