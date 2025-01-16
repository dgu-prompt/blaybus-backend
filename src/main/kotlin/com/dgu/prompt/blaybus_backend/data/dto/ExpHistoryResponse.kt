package com.dgu.prompt.blaybus_backend.data.dto

data class ExpHistoryResponse(
    val expId: Int,
    val employeeNumber: Int,
    val expYear: Int,
    val expDo: Int,
    val expType: String
)
