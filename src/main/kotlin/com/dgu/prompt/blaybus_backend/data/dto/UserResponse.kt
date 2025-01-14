package com.dgu.prompt.blaybus_backend.data.dto

data class UserResponse(
    val employeeNumber: String,
    val employeeName: String,
    val department: String,
    val joinDate: String,
    val level: String,
    val password: String,
    val jobGroupId: Int
)