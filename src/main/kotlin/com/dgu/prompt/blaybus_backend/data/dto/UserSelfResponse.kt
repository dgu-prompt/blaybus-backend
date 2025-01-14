package com.dgu.prompt.blaybus_backend.data.dto

data class UserSelfResponse(
    val employeeNumber: String,
    val employeeName: String,
    val username: String,
    val department: String,
    val joinDate: String,
    val level: String,
    val password: String,
    val jobGroupId: Int,
    val characterUrl: String?
)