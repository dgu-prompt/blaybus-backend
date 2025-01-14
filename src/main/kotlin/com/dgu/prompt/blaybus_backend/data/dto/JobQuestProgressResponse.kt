package com.dgu.prompt.blaybus_backend.data.dto

import com.dgu.prompt.blaybus_backend.data.entity.JobQuestProgress

data class JobQuestProgressResponse(
    val questId: Int,
    val status: String,
    val period: Int,               
    val isCurrentPeriod: Boolean
)

