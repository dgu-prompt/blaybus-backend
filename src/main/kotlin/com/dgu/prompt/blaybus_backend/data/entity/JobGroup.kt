package com.dgu.prompt.blaybus_backend.data.entity

import jakarta.persistence.*

@Entity
@Table(name = "job_group")
data class JobGroup(
    @Id
    @Column(name = "job_group_id")
    val jobGroupId: Int,

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    val department: Department // Foreign key reference to `department`
)
