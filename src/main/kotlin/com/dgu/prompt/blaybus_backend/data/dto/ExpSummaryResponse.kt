package com.dgu.prompt.blaybus_backend.data.dto;

data class ExpSummaryResponse(
    val userId: String,
    val prevYearExp: Int,
    val totalExp: Int,
    val yearlyExp: Int,
    val requiredExp: Int
)
