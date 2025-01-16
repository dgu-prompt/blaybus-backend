package com.dgu.prompt.blaybus_backend.controller

import com.dgu.prompt.blaybus_backend.data.dto.ExpSummaryResponse
import com.dgu.prompt.blaybus_backend.service.ExpService
import com.dgu.prompt.blaybus_backend.security.JwtUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/exps")
class ExpRankController(
    private val expService: ExpService,
    private val jwtUtil: JwtUtil
) {

    @GetMapping("/rank")
    fun getExpRanking(@RequestHeader("Authorization") token: String): ResponseEntity<Any> {
        val jwtToken = if (token.startsWith("Bearer ")) token.substring(7) else token
        val expRanking = expService.getExpRanking(jwtToken)
        return ResponseEntity.ok(expRanking)
    }
}
