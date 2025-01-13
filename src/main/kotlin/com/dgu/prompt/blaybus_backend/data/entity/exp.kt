package com.dgu.prompt.blaybus_backend.data.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "exp")
data class Exp(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY 전략을 사용하여 Auto-Increment 적용
    @Column(name = "exp_id")
    val expId: Int = 0, // 기본값 설정 필요 (JPA에서 초기화 시 사용)

    @Column(name = "employee_number", nullable = false)
    val employeeNumber: Int,

    @Column(name = "exp_year", nullable = false)
    val expYear: Int,

    @Column(name = "exp_do", nullable = false)
    val expDo: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "exp_type", nullable = false)
    val expType: ExpType,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime
)

