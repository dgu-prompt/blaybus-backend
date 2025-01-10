package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.Departments
import org.springframework.data.jpa.repository.JpaRepository

interface DepartmentsRepository : JpaRepository<Departments, String>
