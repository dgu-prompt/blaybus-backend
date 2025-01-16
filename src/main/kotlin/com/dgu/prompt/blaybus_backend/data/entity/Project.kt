package com.dgu.prompt.blaybus_backend.data.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "project")
data class Project(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    val projectId: Int,

//    @Column(name = "employee_number", nullable = false)
//    val employeeNumber: Int,
    @ManyToOne
    @JoinColumn(name = "employee_number", referencedColumnName = "employee_number", nullable = false)
    val user: Users, // Foreign key reference to `Users`

    @Column(name = "project_name", nullable = false)
    val projectName: String,

    @Column(name = "description")
    val description: String? = null,

    @Column(name = "project_exp_do", nullable = false)
    val projectExpDo: Int,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime,

    @Column(name = "project_date", nullable = false)
    val projectDate: LocalDate
)
