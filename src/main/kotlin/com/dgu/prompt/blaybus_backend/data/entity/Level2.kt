package com.dgu.prompt.blaybus_backend.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime


@Entity
@Table(name = "level2")
data class Level2(
    @Id
    @Column(name = "level_id")
    val levelId: String,

    @Column(name = "required_exp_do")
    val requiredExpDo: Long? = null,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime
)
