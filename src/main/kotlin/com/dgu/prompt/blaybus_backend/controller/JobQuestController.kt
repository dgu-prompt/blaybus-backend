package com.dgu.prompt.blaybus_backend.controller

import com.dgu.prompt.blaybus_backend.data.dto.JobQuestResponse
import com.dgu.prompt.blaybus_backend.service.JobQuestService
import com.dgu.prompt.blaybus_backend.security.JwtUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/job-quests")
class JobQuestsController(
    private val jwtUtil: JwtUtil,
    private val jobQuestService: JobQuestService
) {
    @GetMapping("/list")
    fun getJobQuests(
        @RequestParam frequency: String,
        @RequestHeader("Authorization") authToken: String
    ): ResponseEntity<List<JobQuestResponse>> {
        val employeeNumber = jwtUtil.extractEmployeeNumber(authToken.replace("Bearer ", ""))
            ?: throw IllegalArgumentException("Invalid token")
        println("employeeNumber: $employeeNumber")
        val jobQuests = jobQuestService.getJobQuests(employeeNumber, frequency)
        return ResponseEntity.ok(jobQuests)
    }
}
