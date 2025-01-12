package com.dgu.prompt.blaybus_backend.service

import com.dgu.prompt.blaybus_backend.data.dto.JobQuestProgressResponse
import com.dgu.prompt.blaybus_backend.data.dto.JobQuestResponse
import com.dgu.prompt.blaybus_backend.data.entity.FrequencyType
import com.dgu.prompt.blaybus_backend.data.repository.JobQuestProgressRepository
import com.dgu.prompt.blaybus_backend.data.repository.JobQuestRepository
import com.dgu.prompt.blaybus_backend.data.repository.UsersRepository
import com.dgu.prompt.blaybus_backend.security.JwtUtil
import org.springframework.stereotype.Service
import java.util.Calendar
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Service
class JobQuestService(
    private val jwtUtil: JwtUtil,
    private val jobQuestRepository: JobQuestRepository,
    private val jobQuestProgressRepository: JobQuestProgressRepository,
    private val usersRepository: UsersRepository
) {

    fun getJobQuests(employeeNumber: Int, frequency: String): List<JobQuestResponse> {
        val user = usersRepository.findUserByEmployeeNumber(employeeNumber)
            ?: throw IllegalArgumentException("User not found for employeeNumber: $employeeNumber")

    
        val frequencyType = try {
            FrequencyType.valueOf(frequency.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid frequency type: $frequency. Must be 'WEEK' or 'MONTH'")
        }
    
        val currentPeriod = when (frequencyType) {
            FrequencyType.WEEK -> getCurrentWeekOfYear()
            FrequencyType.MONTH -> getCurrentMonthOfYear()
        }
    
        val jobQuests = jobQuestRepository.findByJobGroupIdAndDepartmentIdAndFrequencyType(
            jobGroupId = user.jobGroupId,
            departmentId = user.departmentId,
            frequencyType = frequencyType
        )
    
        return jobQuests.map { jobQuest ->
            val progresses = jobQuestProgressRepository.findByJobQuest_QuestId(
                questId = jobQuest.questId
            ).map { progress ->
                JobQuestProgressResponse(
                    questId = progress.jobQuest.questId,
                    status = progress.status.name,
                    period = progress.period,
                    isCurrentPeriod = (progress.period == currentPeriod)
                )
            }
    
            JobQuestResponse(
                questId = jobQuest.questId,
                maxExpDo = jobQuest.maxExpDo,
                medianExpDo = jobQuest.medianExpDo,
                frequencyType = jobQuest.frequencyType.name,
                departmentId = jobQuest.departmentId,
                questsProgress = progresses
            )
        }
    }
    

    private fun getCurrentWeekOfYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.WEEK_OF_YEAR)
    }

    private fun getCurrentMonthOfYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH는 0부터 시작
    }
}
