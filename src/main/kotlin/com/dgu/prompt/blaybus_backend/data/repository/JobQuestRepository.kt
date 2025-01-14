package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.FrequencyType
import com.dgu.prompt.blaybus_backend.data.entity.JobQuest
import org.springframework.data.jpa.repository.JpaRepository

interface JobQuestRepository : JpaRepository<JobQuest, Int> {
    fun findByJobGroupIdAndDepartmentIdAndFrequencyType(
        jobGroupId: Int,
        departmentId: String,
        frequencyType: FrequencyType
    ): List<JobQuest>
}
