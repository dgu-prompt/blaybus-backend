package com.dgu.prompt.blaybus_backend.data.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "fcm_token")
data class FcmToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "employee_number", nullable = false)
    val user: Users, // Users 테이블과 연관 관계

    @Column(name = "fcm_token", nullable = false, unique = true)
    val fcmToken: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
