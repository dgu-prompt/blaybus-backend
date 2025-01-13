package com.dgu.prompt.blaybus_backend.controller

import com.dgu.prompt.blaybus_backend.data.dto.LeaderQuestResponse
import com.dgu.prompt.blaybus_backend.service.LeaderQuestService
import com.dgu.prompt.blaybus_backend.security.JwtUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/leader-quests")
class LeaderQuestsController(
    private val jwtUtil: JwtUtil,
    private val leaderQuestService: LeaderQuestService
) {
    @GetMapping("/list")
    fun getLeaderQuests(
        @RequestParam frequency: String,
        @RequestHeader("Authorization") authToken: String
    ): ResponseEntity<List<LeaderQuestResponse>> {
        println("Authorization Header: $authToken")
        val employeeNumber = jwtUtil.extractEmployeeNumber(authToken.replace("Bearer ", ""))
            ?: throw IllegalArgumentException("Invalid token")
        println("Extracted Employee Number: $employeeNumber")

        val leaderQuests = leaderQuestService.getLeaderQuests(employeeNumber, frequency)
        println("Leader Quests Response: $leaderQuests")
        return ResponseEntity.ok(leaderQuests)
    }
}
