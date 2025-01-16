package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.Project
import org.springframework.data.jpa.repository.JpaRepository

interface ProjectRepository : JpaRepository<Project, Int> {
    fun findByUser_EmployeeNumber(employeeNumber: Int): List<Project>
fun findByUserEmployeeNumberAndProjectName(employeeNumber: Int, projectName: String): Project?
}
