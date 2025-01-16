package com.dgu.prompt.blaybus_backend.data.dto

data class ProjectResponse(
    val projectId: Int,
    val projectExpDo: Int,
    val projectName: String,
    val description: String?
)
