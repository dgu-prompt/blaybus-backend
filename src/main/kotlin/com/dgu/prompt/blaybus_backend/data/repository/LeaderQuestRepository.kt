package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.LeaderQuest
import com.dgu.prompt.blaybus_backend.data.entity.FrequencyType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface LeaderQuestRepository : JpaRepository<LeaderQuest, Int> {
    fun findByDepartments_DepartmentIdAndFrequencyType(
        @Param("departments") departments: String,
        @Param("frequencyType") frequencyType: FrequencyType
    ): List<LeaderQuest>
}

