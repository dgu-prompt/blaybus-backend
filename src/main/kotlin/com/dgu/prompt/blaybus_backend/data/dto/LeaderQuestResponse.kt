package com.dgu.prompt.blaybus_backend.data.dto

data class LeaderQuestResponse(
    val questId: Int,
    val questTitle: String,
    val maxExpDo: Int,
    val medianExpDo: Int,
    val frequencyType: String,
    val departmentId: String,
    val questsProgress: List<LeaderQuestProgressResponse>
)

data class LeaderQuestProgressResponse(
    val questId: Int,
    val status: String,
    val period: Int,
    val isCurrentPeriod: Boolean
)
