package com.dgu.prompt.blaybus_backend.data.entity

import jakarta.persistence.*

@Entity
@Table(name = "departments")
data class Departments(
    @Id
    @Column(name = "department_id")
    val departmentId: String
)
