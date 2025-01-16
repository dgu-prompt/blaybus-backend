package com.dgu.prompt.blaybus_backend.data.dto

data class ExpRankResponse(
    val employeeNumber: Int,
    val employeeName: String,
    val totalExp: Int,
    val recentLv: String,
    val rank: Int
)
