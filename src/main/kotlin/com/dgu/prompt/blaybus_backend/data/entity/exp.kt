package com.dgu.prompt.blaybus_backend.data.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.Date

@Entity
@Table(name = "exp")
data class Exp(
    @Id
    @Column(name = "exp_id")
    val expId: Int,

    @Column(name = "employee_number", nullable = false)
    val employeeNumber: Int,

    @Column(name = "exp_date", nullable = false)
    @Temporal(TemporalType.DATE)
    val expDate: Date,

    @Column(name = "exp_do", nullable = false)
    val expDo: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "exp_type", nullable = false)
    val expType: ExpType,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime
)

enum class ExpType {
    HR_FIRST, HR_SECOND, JOB_QUEST, LEADER_QUEST, PROJECT
}

