package com.dgu.prompt.blaybus_backend.data.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "users2")
data class Users2(
    @Id
    @Column(name = "employee_number")
    val employeeNumber: Int,

    @Column(name = "level_id", nullable = false)
    val levelId: String,

    @Column(name = "job_group_id", nullable = false)
    val jobGroupId: Int,

    @Column(name = "department_id", nullable = false)
    val departmentId: String,

    @Column(name = "employee_name", nullable = false)
    val employeeName: String,

    @Column(name = "username")
    val username: String? = null,

    @Column(name = "password", nullable = false)
    val password: String,

    @Column(name = "join_date", nullable = false)
    @Temporal(TemporalType.DATE)
    val joinDate: Date,

    @Column(name = "is_admin", nullable = false)
    val isAdmin: Boolean,

    @Column(name = "character_url")
    val characterUrl: String? = null,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime
)
