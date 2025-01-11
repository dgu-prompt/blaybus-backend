package com.dgu.prompt.blaybus_backend.service

import com.dgu.prompt.blaybus_backend.data.data.entity.Level
import com.dgu.prompt.blaybus_backend.data.dto.ExpSummaryResponse
import com.dgu.prompt.blaybus_backend.data.repository.ExpRepository
import com.dgu.prompt.blaybus_backend.data.repository.LevelRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ExpService(
    private val expRepository: ExpRepository,
    private val levelRepository: LevelRepository
) {
    fun getUserExpSummary(userId: Int): ExpSummaryResponse {
        val allExps = expRepository.findAllByEmployeeNumber(userId)

        // 작년까지의 누적 경험치 계산
        val lastYear = LocalDate.now().year - 1
        val prevYearExp = allExps
            .filter { it.expDate < lastYear } // 연도로 비교
            .sumOf { it.expDo }

        // 올해의 누적 경험치 계산
        val thisYear = LocalDate.now().year
        val yearlyExp = allExps
            .filter { it.expDate == thisYear } // 연도로 비교
            .sumOf { it.expDo }

        // 총 경험치 계산
        val totalExp = prevYearExp + yearlyExp

        // 현재 레벨과 다음 레벨까지 필요한 경험치 계산
        val (currentLevel, requiredExp) = calculateCurrentLevelAndRequiredExp(totalExp)

        return ExpSummaryResponse(
            userId = userId.toString(),
            prevYearExp = prevYearExp,
            totalExp = totalExp,
            yearlyExp = yearlyExp,
            requiredExp = requiredExp
        )
    }

    private fun calculateCurrentLevelAndRequiredExp(totalExp: Int): Pair<Level, Int> {
        // 모든 레벨 정보를 필요 경험치 순으로 정렬
        val levels = levelRepository.findAll().sortedBy { it.requiredExpDo }

        // 현재 레벨 및 다음 레벨의 필요 경험치 초기화
        var currentLevel: Level? = null
        var nextLevelRequiredExp: Int? = null

        for (level in levels) {
            if (totalExp < (level.requiredExpDo ?: Int.MAX_VALUE)) {
                nextLevelRequiredExp = level.requiredExpDo?.toInt()
                break
            }
            currentLevel = level
        }

        // 현재 레벨이 없으면 첫 번째 레벨로 설정
        if (currentLevel == null) {
            currentLevel = levels.firstOrNull()
        }

        // 다음 레벨까지 필요한 경험치 계산
        val requiredExp = nextLevelRequiredExp?.let { it - totalExp } ?: 0 // 최고 레벨인 경우 0 반환

        return Pair(currentLevel!!, requiredExp)
    }
}
