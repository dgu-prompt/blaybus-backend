package com.dgu.prompt.blaybus_backend.data.entity;

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.Date

@Entity
// @NoArgsConstructor lombok 추가 고민,,
@Table(name = "users")
data class Users(
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
) {

    // 회원가입 시 사용할 생성자 (username과 password만 전달)
    constructor(username: String, password: String) : this(
        employeeNumber = 0,  // 기본값 설정
        levelId = "",        // 기본값 설정
        jobGroupId = 0,      // 기본값 설정
        departmentId = "",   // 기본값 설정
        employeeName = "",   // 기본값 설정
        username = username, // 전달된 값
        password = password, // 전달된 값
        joinDate = Date(),   // 기본값 설정
        isAdmin = false,     // 기본값 설정
        characterUrl = null, // 기본값 설정
        updatedAt = LocalDateTime.now() // 기본값 설정
    )
}