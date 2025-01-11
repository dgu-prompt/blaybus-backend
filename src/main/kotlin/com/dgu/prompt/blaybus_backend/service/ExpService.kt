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

        // 4. 현재 레벨과 다음 레벨로 필요한 경험치 계산
        val (currentLevel, requiredExp) = calculateCurrentLevelAndRequiredExp(totalExp)

        // 5. 결과 반환
        return ExpSummaryResponse(
            userId = userId.toString(),
            prevYearExp = prevYearExp,
            totalExp = totalExp,
            yearlyExp = yearlyExp,
            requiredExp = requiredExp
        )
    }

    private fun calculateCurrentLevelAndRequiredExp(totalExp: Int): Pair<Level, Int> {
        val levels = levelRepository.findAll().sortedBy { it.requiredExpDo }

        var currentLevel: Level? = null
        var nextLevelRequiredExp: Int? = null

        for (level in levels) {
            val requiredExpDo = level.requiredExpDo ?: Long.MAX_VALUE // Null 처리
            if (totalExp.toLong() < requiredExpDo) { // Long 타입끼리 비교
                nextLevelRequiredExp = requiredExpDo.toInt() // Long -> Int 변환
                break
            }
            currentLevel = level
        }

        if (currentLevel == null) {
            currentLevel = levels.firstOrNull()
        }

        val requiredExp = nextLevelRequiredExp?.let { it - totalExp } ?: 0
        return Pair(currentLevel!!, requiredExp)
    }
}
