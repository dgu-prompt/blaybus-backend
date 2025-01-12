package com.dgu.prompt.blaybus_backend.controller

import com.dgu.prompt.blaybus_backend.data.dto.JobQuestResponse
import com.dgu.prompt.blaybus_backend.service.JobQuestService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/job-quests")
class JobQuestsController(
    private val jobQuestService: JobQuestService
) {
    @GetMapping
    fun getJobQuests(
        @RequestParam frequency: String,
        @RequestHeader("Authorization") authToken: String
    ): ResponseEntity<List<JobQuestResponse>> {
        // 유저 정보 추출
        val token = authToken.removePrefix("Bearer ")
        val jobQuests = jobQuestService.getJobQuests(token, frequency)
        return ResponseEntity.ok(jobQuests)
    }
}
