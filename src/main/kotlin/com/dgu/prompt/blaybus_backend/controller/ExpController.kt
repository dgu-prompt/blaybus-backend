package com.dgu.prompt.blaybus_backend.controller

import com.dgu.prompt.blaybus_backend.data.dto.ExpSummaryResponse
import com.dgu.prompt.blaybus_backend.service.ExpService
import com.dgu.prompt.blaybus_backend.security.JwtUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/exps")
class ExpController(
    private val expService: ExpService,
) {
    @GetMapping("/summary")
    fun getExpSummary(
        @RequestHeader("Authorization") authToken: String
    ): ResponseEntity<ExpSummaryResponse> {
        // "Bearer " 접두사 제거
        val token = authToken.removePrefix("Bearer ")
        val summary = expService.getUserExpSummary(token)
        return ResponseEntity.ok(summary)
    }
}
