package com.dgu.prompt.blaybus_backend.service

import com.dgu.prompt.blaybus_backend.data.entity.Exp
import com.dgu.prompt.blaybus_backend.data.entity.Level
import com.dgu.prompt.blaybus_backend.data.dto.ExpSummaryResponse
import com.dgu.prompt.blaybus_backend.data.dto.ExpRankResponse
import com.dgu.prompt.blaybus_backend.data.repository.ExpRepository
import com.dgu.prompt.blaybus_backend.data.repository.LevelRepository
import com.dgu.prompt.blaybus_backend.data.repository.UsersRepository
import com.dgu.prompt.blaybus_backend.security.JwtUtil
import org.springframework.stereotype.Service

@Service
class ExpService(
    private val expRepository: ExpRepository,
    private val levelRepository: LevelRepository,
    private val usersRepository: UsersRepository,
    private val jwtUtil: JwtUtil // JwtUtil 추가
) {
    fun getUserExpSummary(authToken: String): ExpSummaryResponse {
        // JWT에서 employeeNumber 추출
        val employeeNumber = jwtUtil.extractEmployeeNumber(authToken)
            ?: throw IllegalArgumentException("Invalid JWT token: employeeNumber not found")

        val allExps = expRepository.findAllByEmployeeNumber(employeeNumber)
        val currentYear = 2024

        // 작년까지의 누적 경험치 계산
        val prevYearExp = allExps
            .filter { it.expYear <= (currentYear - 1) }
            .sumOf { it.expDo }

        // 올해의 누적 경험치 계산
        val yearlyExp = allExps
            .filter { it.expYear == currentYear }
            .sumOf { it.expDo }

        // 총 누적 경험치 계산
        val totalExp = prevYearExp + yearlyExp

        // 현재 레벨과 다음 레벨 정보 계산
        val (recentLv, nextLv, requiredExp) = calculateLevelsAndRequiredExp(employeeNumber, totalExp)

        // 결과 반환
        return ExpSummaryResponse(
            employeeNumber = employeeNumber,
            prevYearExp = prevYearExp,
            totalExp = totalExp,
            yearlyExp = yearlyExp,
            requiredExp = requiredExp,
            recentLv = recentLv,
            nextLv = nextLv
        )
    }

    fun getExpRanking(authToken: String): List<ExpRankResponse> {
        val requestingEmployeeNumber = jwtUtil.extractEmployeeNumber(authToken)
            ?: throw IllegalArgumentException("Invalid JWT token")
    
        val currentYear = 2024
        val allExpData = expRepository.findAll()
        val yearlyExpByEmployee = allExpData.filter { it.expYear == currentYear }
            .groupBy { it.employeeNumber }
            .mapValues { entry -> entry.value.sumOf { it.expDo } }
    
        return yearlyExpByEmployee.entries
            .sortedByDescending { it.value }
            .mapIndexed { index, entry ->
                val employeeNumber = entry.key
                val totalExp = entry.value
    
                // 유저 정보 조회
                val user = usersRepository.findUserByEmployeeNumber(employeeNumber)
                val employeeName = user?.employeeName ?: "Unknown"
                val recentLv = if (user != null) calculateRecentLevel(user.levelId, totalExp) else "Unknown"
    
                ExpRankResponse(
                    employeeNumber = employeeNumber,
                    employeeName = employeeName,
                    totalExp = totalExp, // 2024년의 경험치만 반환
                    recentLv = recentLv,
                    rank = index + 1
                )
            }
    }

    private fun calculateRecentLevel(levelId: String, totalExp: Int): String {
        // 유저의 현재 레벨 필드값에서 앞글자 추출 (예: F, B)
        val userLevelGroup = levelId.substringBefore("-").substring(0, 1) // 유저의 레벨 앞글자(F, B 등)
    
        // 동일 그룹의 레벨 데이터 필터링
        val levels = levelRepository.findAll()
            .filter { it.levelId.startsWith(userLevelGroup) }
            .sortedBy { it.requiredExpDo }
    
        var recentLv = "Unknown"
    
        for ((index, level) in levels.withIndex()) {
            val levelExp = level.requiredExpDo ?: Long.MAX_VALUE
            if (totalExp.toLong() < levelExp) {
                recentLv = if (index > 0) levels[index - 1].levelId else "Unknown"
                break
            }
        }
    
        if (recentLv == "Unknown" && levels.isNotEmpty()) {
            recentLv = levels.first().levelId
        }
    
        return recentLv
    }
    



    private fun calculateLevelsAndRequiredExp(employeeNumber: Int, totalExp: Int): Triple<String, String, Int> {
        // 유저의 현재 레벨 필드값에서 앞글자 추출 (예: F, B)
        val userLevelGroup = getUserLevelGroup(employeeNumber) // 유저의 레벨 앞글자(F, B 등)

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

    private fun getUserLevelGroup(employeeNumber: Int): String {
        // 유저 정보에서 현재 레벨 ID를 가져옴 (예: F2-I, B1 등)
        val userLevelId = usersRepository.findUserLevelByEmployeeNumber(employeeNumber)
            ?: throw IllegalArgumentException("User with ID $employeeNumber not found")

        // 레벨 ID의 첫 글자 추출 (예: F2-I -> F)
        return userLevelId.substringBefore("-").substring(0, 1)
    }
}
