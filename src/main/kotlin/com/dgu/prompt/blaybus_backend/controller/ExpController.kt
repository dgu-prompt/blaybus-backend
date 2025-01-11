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
    @GetMapping("/summary/{userId}")
    fun getExpSummary(
        @PathVariable userId: Int,
        // @RequestHeader("Authorization") authToken: String
    ): ResponseEntity<ExpSummaryResponse> {
        // Authorization 검증 로직 추가 가능
        val summary = expService.getUserExpSummary(userId)
        return ResponseEntity.ok(summary)
    }
}
