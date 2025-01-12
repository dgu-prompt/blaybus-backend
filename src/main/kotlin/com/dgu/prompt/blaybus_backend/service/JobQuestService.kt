package com.dgu.prompt.blaybus_backend.service

import com.dgu.prompt.blaybus_backend.data.dto.JobQuestProgressResponse
import com.dgu.prompt.blaybus_backend.data.dto.JobQuestResponse
import com.dgu.prompt.blaybus_backend.data.entity.FrequencyType
import com.dgu.prompt.blaybus_backend.data.repository.JobQuestProgressRepository
import com.dgu.prompt.blaybus_backend.data.repository.JobQuestRepository
import com.dgu.prompt.blaybus_backend.security.JwtUtil
import org.springframework.stereotype.Service

@Service
class JobQuestService(
    private val jwtUtil: JwtUtil,
    private val jobQuestRepository: JobQuestRepository,
    private val jobQuestProgressRepository: JobQuestProgressRepository,
) {
    fun getJobQuests(authToken: String, frequency: String): List<JobQuestResponse> {
        // Step 1: JWT에서 유저 정보 추출
        val employeeNumber = jwtUtil.extractEmployeeNumber(authToken)
            ?: throw IllegalArgumentException("Invalid token: missing employeeNumber")
            val departmentId = jwtUtil.extractClaim(authToken) { it["departmentId"] as? String }
            ?: throw IllegalArgumentException("Invalid token: departmentId is missing")
        
        val jobGroupId = jwtUtil.extractClaim(authToken) { it["jobGroupId"] as? Int }
            ?: throw IllegalArgumentException("Invalid token: jobGroupId is missing")
        

        // Step 2: Frequency 검증
        val freqType = try {
            FrequencyType.valueOf(frequency.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid frequency type: $frequency")
        }

        // Step 3: job_quest 조회
        val quests = jobQuestRepository.findByJobGroupIdAndDepartmentIdAndFrequencyType(jobGroupId, departmentId, freqType)

        // Step 4: 각 job_quest에 대한 progress 조회
        return quests.map { quest ->
            val progress = jobQuestProgressRepository.findByJobQuest_QuestIdAndUser_EmployeeNumber(quest.questId, employeeNumber)
            JobQuestResponse(
                questId = quest.questId,
                maxExpDo = quest.maxExpDo,
                medianExpDo = quest.medianExpDo,
                frequencyType = quest.frequencyType.name,
                departmentId = quest.departmentId,
                questsProgress = progress.map { JobQuestProgressResponse(it) }
            )
        }
    }
}
