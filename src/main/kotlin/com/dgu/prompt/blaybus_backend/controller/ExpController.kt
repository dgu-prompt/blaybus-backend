package com.dgu.prompt.blaybus_backend.controller

import com.dgu.prompt.blaybus_backend.data.dto.ExpSummaryResponse
import com.dgu.prompt.blaybus_backend.service.ExpService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/exps")
class ExpController(
    private val expService: ExpService
) {
    @GetMapping("/summary/{employeeNumber}")
    fun getExpSummary(
        @PathVariable employeeNumber: Int,
        // @RequestHeader("Authorization") authToken: String
    ): ResponseEntity<ExpSummaryResponse> { 
        // Authorization 검증 로직 추가 가능
        val summary = expService.getUserExpSummary(employeeNumber)
        return ResponseEntity.ok(summary)
    }
}
