package com.dgu.prompt.blaybus_backend.data.dto

import com.dgu.prompt.blaybus_backend.data.entity.JobQuestProgress

data class JobQuestProgressResponse(
    val questId: Int,
    val status: String,
    val period: Int
) {
    constructor(progress: JobQuestProgress) : this(
        questId = progress.jobQuest.questId,
        status = progress.status.name,
        period = progress.period
    )
}
