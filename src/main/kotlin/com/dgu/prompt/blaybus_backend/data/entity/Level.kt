package com.dgu.prompt.blaybus_backend.data.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "level")
data class Level(
    @Id
    @Column(name = "level_id")
    val levelId: String,

    @Column(name = "required_exp_do")
    val requiredExpDo: Long? = null,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime
)
