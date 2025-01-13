package com.dgu.prompt.blaybus_backend.data.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "leader_quest2")
data class LeaderQuest2(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_id")
    val questId: Int,

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    val departments: Departments, // Foreign key reference to `department` table

    @Column(name = "quest_title", nullable = false)
    val questTitle: String,

    @Column(name = "description")
    val description: String? = null,

    @Column(name = "max_condition")
    val maxCondition: String? = null,

    @Column(name = "median_condition")
    val medianCondition: String? = null,

    @Column(name = "max_exp_do", nullable = false)
    val maxExpDo: Int,

    @Column(name = "median_exp_do", nullable = false)
    val medianExpDo: Int,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime,

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency_type", nullable = false)
    val frequencyType: FrequencyType
)
