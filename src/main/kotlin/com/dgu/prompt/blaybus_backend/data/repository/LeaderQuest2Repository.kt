package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.FrequencyType
import com.dgu.prompt.blaybus_backend.data.entity.LeaderQuest2
import com.dgu.prompt.blaybus_backend.data.entity.Departments
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LeaderQuest2Repository : JpaRepository<LeaderQuest2, Int> {
    fun findByDepartmentsAndQuestTitleAndFrequencyType(
        departments: Departments, // `Departments` 엔티티 타입
        questTitle: String,
        frequencyType: FrequencyType
    ): Optional<LeaderQuest2>
}
