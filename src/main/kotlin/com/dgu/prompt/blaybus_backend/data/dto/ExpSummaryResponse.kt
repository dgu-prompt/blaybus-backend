package com.dgu.prompt.blaybus_backend.data.dto

data class ExpSummaryResponse(
    val employeeNumber: Int,
    val prevYearExp: Int,
    val totalExp: Int,
    val yearlyExp: Int,
    val requiredExp: Int,
    val recentLv: String,
    val nextLv: String
)
