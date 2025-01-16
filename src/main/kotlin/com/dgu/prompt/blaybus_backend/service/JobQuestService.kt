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
import java.util.Date
import java.text.SimpleDateFormat

@Service
class JobQuestService(
    private val jwtUtil: JwtUtil,
    private val jobQuestRepository: JobQuestRepository,
    private val jobQuestProgressRepository: JobQuestProgressRepository,
    private val usersRepository: UsersRepository
) {

    private val mockCurrentDate: Date? = SimpleDateFormat("yyyy-MM-dd").parse("2024-12-25") // 테스트용 날짜

    fun getJobQuests(employeeNumber: Int, frequency: String): List<JobQuestResponse> {
        println("Received employeeNumber in service: $employeeNumber")
        val user = usersRepository.findUserByEmployeeNumber(employeeNumber)
            ?: throw IllegalArgumentException("User not found for employeeNumber: $employeeNumber")

        val frequencyType = try {
            FrequencyType.valueOf(frequency.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid frequency type: $frequency. Must be 'WEEK' or 'MONTH'")
        }
        println("employeeNumber passed to frequencyType: $employeeNumber")

        val currentPeriod = when (frequencyType) {
            FrequencyType.WEEK -> getCurrentWeekOfYear()
            FrequencyType.MONTH -> getCurrentMonthOfYear()
        }

        val jobQuests = jobQuestRepository.findByJobGroupIdAndDepartmentIdAndFrequencyType(
            jobGroupId = user.jobGroupId,
            departmentId = user.departmentId,
            frequencyType = frequencyType
        )
        println("employeeNumber passed to jobQuests: $employeeNumber")

        return jobQuests.map { jobQuest ->
            val progresses = jobQuestProgressRepository.findByJobQuest_QuestId(
                questId = jobQuest.questId
            ).map { progress ->
                println("Progress fetched for questId ${jobQuest.questId}: $progress")
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
