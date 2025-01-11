package com.dgu.prompt.blaybus_backend.service

import com.dgu.prompt.blaybus_backend.data.entity.Level
import com.dgu.prompt.blaybus_backend.dto.ExpSummaryResponse
import com.dgu.prompt.blaybus_backend.repository.ExpRepository
import com.dgu.prompt.blaybus_backend.repository.LevelRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ExpService(
    private val expRepository: ExpRepository,
    private val levelRepository: LevelRepository
) {
    fun getUserExpSummary(userId: Int): ExpSummaryResponse {
        val allExps = expRepository.findAllByEmployeeNumber(userId)

        // 작년 누적 경험치 계산
        val lastYear = LocalDate.now().year - 1
        val prevYearExp = allExps
            .filter { it.expDate.year < lastYear }
            .sumOf { it.expDo }

        // 올해 누적 경험치 계산
        val thisYear = LocalDate.now().year
        val yearlyExp = allExps
            .filter { it.expDate.year == thisYear }
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
        // 모든 레벨 정보를 경험치 순으로 정렬하여 가져옴
        val levels = levelRepository.findAll().sortedBy { it.requiredExpDo }

        // 현재 레벨과 다음 레벨의 필요 경험치를 초기화
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
        val requiredExp = if (nextLevelRequiredExp != null) {
            nextLevelRequiredExp - totalExp
        } else {
            0 // 최고 레벨인 경우 추가 경험치 필요 없음
        }

        return Pair(currentLevel!!, requiredExp)
    }
}
