package com.dgu.prompt.blaybus_backend.data.entity

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "job_group")
data class JobGroup(
    @EmbeddedId
    val id: JobGroupId,

    @ManyToOne
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    val departments: Departments
)

@Embeddable
data class JobGroupId(
    @Column(name = "job_group_id", nullable = false)
    val jobGroupId: Int,

    @Column(name = "department_id", nullable = false)
    val departmentId: String
) : Serializable
