package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.Users
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UsersRepository : JpaRepository<Users, Int> {
    @Query("SELECT u.levelId FROM Users u WHERE u.employeeNumber = :employeeNumber")

    fun findUserLevelByEmployeeNumber(@Param("employeeNumber") employeeNumber: Int): String?

    fun findByUsername(username: String): Users?
}
