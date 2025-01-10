package com.dgu.prompt.blaybus_backend.data.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "notification")
data class Notification(
    @Id
    @Column(name = "notification_id")
    val notificationId: Int,

    @ManyToOne
    @JoinColumn(name = "employee_number", nullable = false)
    val user: User, // Foreign key reference to `users` table

    @Column(name = "content", nullable = false)
    val content: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: NotificationType,

    @Column(name = "created_at")
    val createdAt: LocalDateTime? = null,

    @Column(name = "is_read", nullable = false)
    val isRead: Boolean,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime
)

enum class NotificationType {
    EXP, POST, SUCCESS
}
