package com.dgu.prompt.blaybus_backend.data.dto

data class JobQuestResponse(
    val questId: Int,
    val maxExpDo: Int,
    val medianExpDo: Int,
    val frequencyType: String,
    val departmentId: String,
    val questsProgress: List<JobQuestProgressResponse>
)
