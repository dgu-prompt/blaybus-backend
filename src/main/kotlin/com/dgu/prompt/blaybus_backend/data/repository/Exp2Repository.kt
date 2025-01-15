package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.Exp2
import com.dgu.prompt.blaybus_backend.data.entity.FrequencyType
import com.dgu.prompt.blaybus_backend.data.entity.LeaderQuest2
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface Exp2Repository : JpaRepository<Exp2, Int> {
//    fun findByDepartmentIdAndQuestTitleAndFrequencyType(
//        departmentId: String,
//        questTitle: String,
//        frequencyType: FrequencyType
//    ): Optional<LeaderQuest2>

}
