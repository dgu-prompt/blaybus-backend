package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.Users
import com.dgu.prompt.blaybus_backend.data.entity.Users2
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface UsersRepository : JpaRepository<Users, Int> {
    @Query("SELECT u.levelId FROM Users u WHERE u.employeeNumber = :employeeNumber")
    fun findUserLevelByEmployeeNumber(@Param("employeeNumber") employeeNumber: Int): String?

    @Query("SELECT u FROM Users u WHERE u.employeeNumber = :employeeNumber")
    fun findUserByEmployeeNumber(@Param("employeeNumber") employeeNumber: Int): Users?

    fun findByUsername(username: String): Users?
    fun findByEmployeeNumber(employeeNumber: Int): List<Users?>
    //fun findByEmployeeNumber2(employeeNumber: Int): <Users?>

    fun findOptionalByEmployeeNumber(employeeNumber: Int): Optional<Users>

    fun findAUserByEmployeeNumber(employeeNumber: Int): Users?

}
