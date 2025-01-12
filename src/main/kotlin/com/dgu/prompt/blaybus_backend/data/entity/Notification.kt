package com.dgu.prompt.blaybus_backend.data.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "notification")
data class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성
    @Column(name = "notification_id")
    val notificationId: Int = 0,

    @ManyToOne
    @JoinColumn(name = "employee_number", nullable = false)
    val users: Users, // Foreign key reference to `users` table

    @Column(name = "content", nullable = false)
    val content: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: NotificationType,

    @Column(name = "created_at")
    val createdAt: LocalDateTime? = null,

    @Column(name = "is_read", nullable = false)
    var isRead: Boolean, // isRead는 변경 가능한 값

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime
)

enum class NotificationType {
    EXP, POST, SUCCESS
}
