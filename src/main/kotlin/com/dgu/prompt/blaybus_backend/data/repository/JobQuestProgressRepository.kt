package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.JobQuestProgress
import org.springframework.data.jpa.repository.JpaRepository

interface JobQuestProgressRepository : JpaRepository<JobQuestProgress, Int> {
    fun findByJobQuest_QuestIdAndUser_EmployeeNumber(questId: Int, employeeNumber: Int): List<JobQuestProgress>
}

