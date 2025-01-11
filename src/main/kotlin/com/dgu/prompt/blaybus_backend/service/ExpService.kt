package com.dgu.prompt.blaybus_backend.service

import com.dgu.prompt.blaybus_backend.data.entity.Exp
import com.dgu.prompt.blaybus_backend.data.entity.Level
import com.dgu.prompt.blaybus_backend.data.dto.ExpSummaryResponse
import com.dgu.prompt.blaybus_backend.data.repository.ExpRepository
import com.dgu.prompt.blaybus_backend.data.repository.LevelRepository
import org.springframework.stereotype.Service

@Service
class ExpService(
    private val expRepository: ExpRepository,
    private val levelRepository: LevelRepository
) {
    fun getUserExpSummary(userId: Int): ExpSummaryResponse {
        val allExps = expRepository.findAllByEmployeeNumber(userId)
        val currentYear = 2024 // 현재 연도 고정

        // 1. 작년까지의 누적 경험치 계산
        val prevYearExp = allExps
            .filter { it.expYear <= (currentYear - 1) } // 2023년까지 포함
            .sumOf { it.expDo }

        // 2. 올해의 누적 경험치 계산
        val yearlyExp = allExps
            .filter { it.expYear == currentYear } // 2024년 데이터만 포함
            .sumOf { it.expDo }

        // 3. 총 누적 경험치 계산
        val totalExp = prevYearExp + yearlyExp

        // 4. 현재 레벨과 다음 레벨 정보 계산
        val (recentLv, nextLv, requiredExp) = calculateLevelsAndRequiredExp(userId, totalExp)

        // 5. 결과 반환
        return ExpSummaryResponse(
            userId = userId.toString(),
            prevYearExp = prevYearExp,
            totalExp = totalExp,
            yearlyExp = yearlyExp,
            requiredExp = requiredExp,
            recentLv = recentLv,
            nextLv = nextLv
        )
    }

    private fun calculateLevelsAndRequiredExp(userId: Int, totalExp: Int): Triple<String, String, Int> {
        // 유저의 현재 레벨 필드값에서 앞글자 추출 (예: F, B)
        val userLevelGroup = getUserLevelGroup(userId) // 유저의 레벨 앞글자(F, B 등)

        // 동일 그룹의 레벨 데이터 필터링
        val levels = levelRepository.findAll()
            .filter { it.levelId.startsWith(userLevelGroup) }
            .sortedBy { it.requiredExpDo }

        var recentLv = "Unknown"
        var nextLv = "Max Level"
        var requiredExp = 0

        for ((index, level) in levels.withIndex()) {
            val levelExp = level.requiredExpDo ?: Long.MAX_VALUE
            if (totalExp.toLong() < levelExp) {
                recentLv = if (index > 0) levels[index - 1].levelId else "Unknown"
                nextLv = level.levelId
                requiredExp = levelExp.toInt()
                break
            }
        }

        if (recentLv == "Unknown" && levels.isNotEmpty()) {
            recentLv = levels.first().levelId
        }

        return Triple(recentLv, nextLv, requiredExp)
    }

    private fun getUserLevelGroup(userId: Int): String {
        // 1. 유저 정보에서 현재 레벨 ID를 가져옴 (예: F2-I, B1 등)
        val userLevelId = expRepository.findUserLevelByEmployeeNumber(userId)
            ?: throw IllegalArgumentException("User with ID $userId not found")

        // 2. 레벨 ID의 첫 글자 추출 (예: F2-I -> F)
        return userLevelId.substringBefore("-").substring(0, 1)
    }
}
