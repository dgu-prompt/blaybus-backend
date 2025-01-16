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
import java.util.Date
import java.text.SimpleDateFormat

@Service
class LeaderQuestService(
    private val leaderQuestRepository: LeaderQuestRepository,
    private val leaderQuestProgressRepository: LeaderQuestProgressRepository,
    private val usersRepository: UsersRepository
) {
    private val mockCurrentDate: Date? = SimpleDateFormat("yyyy-MM-dd").parse("2024-12-25") // 테스트용 날짜

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
            // Progress 데이터 가져오기
            val progresses = leaderQuestProgressRepository.findByLeaderQuest_QuestIdAndUser_EmployeeNumber(
                questId = leaderQuest.questId,
                employeeNumber = employeeNumber
            )
    
            // 현재 Progress 데이터에서 최대 기간 계산
            val maxPeriod = progresses.maxOfOrNull { it.period } ?: 0
            println("Max period for LeaderQuest ${leaderQuest.questId}: $maxPeriod")
    
            // 빈 기간 포함한 Progress 데이터 생성
            val allPeriods = (1..maxPeriod)
            val progressMap = progresses.associateBy { it.period }
    
            val completeProgresses = allPeriods.map { period ->
                progressMap[period]?.let { progress ->
                    LeaderQuestProgressResponse(
                        questId = progress.leaderQuest.questId,
                        status = progress.status.name,
                        period = progress.period,
                        isCurrentPeriod = (progress.period == currentPeriod)
                    )
                } ?: LeaderQuestProgressResponse(
                    questId = leaderQuest.questId,
                    status = "PENDING",
                    period = period,
                    isCurrentPeriod = (period == currentPeriod)
                )
            }
    
            LeaderQuestResponse(
                questId = leaderQuest.questId,
                questTitle = leaderQuest.questTitle,
                maxExpDo = leaderQuest.maxExpDo,
                medianExpDo = leaderQuest.medianExpDo,
                frequencyType = leaderQuest.frequencyType.name,
                departmentId = leaderQuest.departments.departmentId,
                questsProgress = completeProgresses
            )
        }
    }
    
    private fun getCurrentWeekOfYear(): Int {
        val calendar = Calendar.getInstance()
        if (mockCurrentDate != null) {
            calendar.time = mockCurrentDate
        }

        // 기준: 해당 해의 1월 1일
        val yearStart = Calendar.getInstance()
        yearStart.set(calendar.get(Calendar.YEAR), Calendar.JANUARY, 1)
        yearStart.set(Calendar.HOUR_OF_DAY, 0)
        yearStart.set(Calendar.MINUTE, 0)
        yearStart.set(Calendar.SECOND, 0)
        yearStart.set(Calendar.MILLISECOND, 0)

        // 1월 1일이 속한 주의 월요일
        while (yearStart.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            yearStart.add(Calendar.DAY_OF_YEAR, -1)
        }

        // 현재 날짜가 몇 번째 주인지 계산
        val diff = (calendar.timeInMillis - yearStart.timeInMillis) / (1000 * 60 * 60 * 24)
        return (diff / 7 + 1).toInt()
    }

    private fun getCurrentMonthOfYear(): Int {
        val calendar = Calendar.getInstance()
        if (mockCurrentDate != null) {
            calendar.time = mockCurrentDate
        }
        return calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH는 0부터 시작
    }
}
