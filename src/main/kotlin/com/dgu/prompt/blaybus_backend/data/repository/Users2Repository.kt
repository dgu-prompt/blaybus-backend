package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.Users2
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface Users2Repository : JpaRepository<Users2, Int> {
    fun findByEmployeeNumber(employeeNumber: Int): Users2?
    fun findOptionalByEmployeeNumber(employeeNumber: Int): Optional<Users2>

}