package com.dgu.prompt.blaybus_backend.data.dto

data class UserSelfUpdateRequest(
    val password: String? = null,
    val character: String? = null
)