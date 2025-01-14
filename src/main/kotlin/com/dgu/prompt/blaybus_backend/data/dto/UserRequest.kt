package com.dgu.prompt.blaybus_backend.data.dto

import java.util.*

data class UserRequest(
    val employeeNumber: String, // 사번
    val employeeName: String,   // 이름
    val username: String,       // 사용자 ID
    val department: String,     // 소속
    val joinDate: String,       // 입사일 (yyyy-MM-dd)
    val level: String,          // 레벨
    val jobGroupId: Int         // 직무 그룹 ID
)
