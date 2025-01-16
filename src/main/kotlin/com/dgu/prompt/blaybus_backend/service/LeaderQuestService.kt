// LeaderQuestService
package com.dgu.prompt.blaybus_backend.service

import com.dgu.prompt.blaybus_backend.data.dto.LeaderQuestProgressResponse
import com.dgu.prompt.blaybus_backend.data.dto.LeaderQuestResponse
import com.dgu.prompt.blaybus_backend.data.entity.FrequencyType
import com.dgu.prompt.blaybus_backend.data.repository.LeaderQuestProgressRepository
import com.dgu.prompt.blaybus_backend.data.repository.LeaderQuestRepository
import com.dgu.prompt.blaybus_backend.data.repository.UsersRepository
import org.springframework.stereotype.Service
import java.util.Calendar

@Service
class LeaderQuestService(
    private val leaderQuestRepository: LeaderQuestRepository,
    private val leaderQuestProgressRepository: LeaderQuestProgressRepository,
    private val usersRepository: UsersRepository
) {
    fun getLeaderQuests(employeeNumber: Int, frequency: String): List<LeaderQuestResponse> {
        println("Fetching user for employeeNumber: $employeeNumber")
        val user = usersRepository.findUserByEmployeeNumber(employeeNumber)
            ?: throw IllegalArgumentException("User not found for employeeNumber: $employeeNumber")
        println("Found user: $user")

        val frequencyType = try {
            FrequencyType.valueOf(frequency.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid frequency type: $frequency. Must be 'WEEK' or 'MONTH'")
        }
        println("Frequency Type: $frequencyType")

        val currentPeriod = when (frequencyType) {
            FrequencyType.WEEK -> getCurrentWeekOfYear()
            FrequencyType.MONTH -> getCurrentMonthOfYear()
        }

        val leaderQuests = leaderQuestRepository.findByDepartments_DepartmentIdAndFrequencyType(
            departments = user.departmentId,
            frequencyType = frequencyType
        )
        println("Leader Quests Fetched: $leaderQuests")

        return leaderQuests.map { leaderQuest ->
            val progresses = leaderQuestProgressRepository.findByLeaderQuest_QuestIdAndUser_EmployeeNumber(
                questId = leaderQuest.questId,
                employeeNumber = employeeNumber
            ).map { progress ->
                LeaderQuestProgressResponse(
                    questId = progress.leaderQuest.questId,
                    status = progress.status.name,
                    period = progress.period,
                    isCurrentPeriod = (progress.period == currentPeriod)
                )
            }

            LeaderQuestResponse(
                questId = leaderQuest.questId,
                questTitle = leaderQuest.questTitle,
                maxExpDo = leaderQuest.maxExpDo,
                medianExpDo = leaderQuest.medianExpDo,
                frequencyType = leaderQuest.frequencyType.name,
                departmentId = leaderQuest.departments.departmentId,
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
