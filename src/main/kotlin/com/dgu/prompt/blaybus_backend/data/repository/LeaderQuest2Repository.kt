package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.FrequencyType
import com.dgu.prompt.blaybus_backend.data.entity.LeaderQuest2
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface LeaderQuest2Repository : JpaRepository<LeaderQuest2, Int> {
//    fun findByDepartmentsAndQuestTitleAndFrequencyType(
//        departments: String?,
//        questTitle: String?,
//        frequencyType: FrequencyType?
//    ): Optional<LeaderQuest2?>?


}