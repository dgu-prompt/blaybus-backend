package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.Exp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExpRepository : JpaRepository<Exp, Int> {
    fun findAllByEmployeeNumber(employeeNumber: Int): List<Exp>
}
