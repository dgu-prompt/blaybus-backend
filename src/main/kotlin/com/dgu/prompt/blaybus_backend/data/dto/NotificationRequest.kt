package com.dgu.prompt.blaybus_backend.data.dto

import com.dgu.prompt.blaybus_backend.data.entity.NotificationType


data class NotificationRequest(
    val employeeNumber: Int,
    val type: NotificationType,
    val title: String = "",
    val points: Int = 0,
    val postTitle: String = "",
    val period: String = "" // "weekly", "monthly" etc.
)
