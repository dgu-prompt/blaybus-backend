package com.dgu.prompt.blaybus_backend.service

import com.dgu.prompt.blaybus_backend.data.dto.ExpHistoryResponse
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
            ExpHistoryResponse(
                expId = exp.expId,
                employeeNumber = employeeNumber,
                expYear = exp.expYear,
                expDo = exp.expDo,
                expType = exp.expType.name
            )
        }
    }
}
