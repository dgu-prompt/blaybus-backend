package com.dgu.prompt.blaybus_backend.service

import com.dgu.prompt.blaybus_backend.data.dto.ExpHistoryResponse
import com.dgu.prompt.blaybus_backend.data.entity.ExpType
import com.dgu.prompt.blaybus_backend.data.repository.ExpHistoryRepository
import com.dgu.prompt.blaybus_backend.security.JwtUtil
import org.springframework.stereotype.Service

@Service
class ExpHistoryService(
    private val jwtUtil: JwtUtil,
    private val expHistoryRepository: ExpHistoryRepository
) {
    fun getExpHistoryByEmployeeNumber(employeeNumber: Int): List<ExpHistoryResponse> {

        val expEntities = expHistoryRepository.findByEmployeeNumber(employeeNumber)
        return expEntities.map { exp ->
            val hrTier = if (exp.expType == ExpType.HR_FIRST || exp.expType == ExpType.HR_SECOND) {
                when {
                    exp.expDo >= 6500 -> "S등급"
                    exp.expDo >= 4500 -> "A등급"
                    exp.expDo >= 3000 -> "B등급"
                    exp.expDo >= 1500 -> "C등급"
                    else -> "D등급"
                }
            } else {
                null // HR 관련이 아닌 경우 null
            }

            ExpHistoryResponse(
                expId = exp.expId,
                employeeNumber = employeeNumber,
                expYear = exp.expYear,
                expDo = exp.expDo,
                expType = exp.expType.name,
                hrTier = hrTier
            )
        }
    }
}
