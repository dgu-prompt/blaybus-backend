package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.Departments
import com.dgu.prompt.blaybus_backend.data.entity.LeaderQuest
import com.dgu.prompt.blaybus_backend.data.entity.FrequencyType
import com.dgu.prompt.blaybus_backend.data.entity.LeaderQuest2
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface LeaderQuestRepository : JpaRepository<LeaderQuest, Int> {
    fun findByDepartments_DepartmentIdAndFrequencyType(
        @Param("departments") departments: String,
        @Param("frequencyType") frequencyType: FrequencyType
    ): List<LeaderQuest>

    fun findByDepartmentsAndQuestTitleAndFrequencyType(
        departments: Departments, // `Departments` 엔티티 타입
        questTitle: String,
        frequencyType: FrequencyType
    ): Optional<LeaderQuest>
}

