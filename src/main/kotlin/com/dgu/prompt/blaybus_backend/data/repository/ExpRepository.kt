package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.Exp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ExpRepository : JpaRepository<Exp, Int> {
    fun findAllByEmployeeNumber(employeeNumber: Int): List<Exp>

    @Query("SELECT u.levelId FROM User u WHERE u.employeeNumber = :employeeNumber")
    fun findUserLevelByEmployeeNumber(employeeNumber: Int): String?
}
