package com.dgu.prompt.blaybus_backend.controller

import com.dgu.prompt.blaybus_backend.security.JwtUtil
import com.dgu.prompt.blaybus_backend.googleSheetsService.*
import com.dgu.prompt.blaybus_backend.service.AdminUserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/sheet-sync")
class GoogleSheetController(
    private val usersSheetService: UsersSheetService,
    private val levelSheetService: LevelSheetService,
    private val postSheetService: PostSheetService,
    private val leaderQuestSheetService: LeaderQuestSheetService,
    private val jobQuestSheetService: JobQuestSheetService,
    private val expSheetService: ExpSheetService,
    private val projectSheetService: ProjectSheetService,
    private val PRSheetService: PRSheetService,
    private val adminUserService: AdminUserService,
    private val jwtUtil: JwtUtil
) {
    @PostMapping
    fun sheetSync(
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<String> {
        val employeeNumber = jwtUtil.extractEmployeeNumber(token.replace("Bearer ", ""))
            ?: throw IllegalArgumentException("Invalid token")
        val adminUser = adminUserService.getUserByEmployeeNumber(employeeNumber)
        if (!adminUser.isAdmin) {
            throw org.springframework.security.access.AccessDeniedException("Admin privileges required")
        }

        val syncMessages = mutableListOf<String>()
        val syncErrorMessages = mutableListOf<String>()

        syncMessages.add("[")

        // 동기화 작업
        try {
            projectSheetService.syncProjectData()
            syncMessages.add("Project ")
        } catch (e: Exception) {
            syncErrorMessages.add("Error synchronizing project data: ${e.message}")
        }

        try {
            levelSheetService.syncLevels()
            syncMessages.add("Level ")
        } catch (e: Exception) {
            syncErrorMessages.add("Error synchronizing level data: ${e.message}")
        }

        try {
            postSheetService.syncPosts()
            syncMessages.add("Post ")
        } catch (e: Exception) {
            syncErrorMessages.add("Error synchronizing post data: ${e.message}")
        }

        try {
            leaderQuestSheetService.syncLeaderQuestData()
            syncMessages.add("LeaderQuest ")
        } catch (e: Exception) {
            syncErrorMessages.add("Error synchronizing LeaderQuest data: ${e.message}")
        }

        try {
            leaderQuestSheetService.syncLeaderQuestProgressData()
            syncMessages.add("LeaderQuestProgress ")
        } catch (e: Exception) {
            syncErrorMessages.add("Error synchronizing LeaderQuestProgress data: ${e.message}")
        }

        try {
            jobQuestSheetService.syncJobQuestData()
            syncMessages.add("JobQuest ")
        } catch (e: Exception) {
            syncErrorMessages.add("Error synchronizing JobQuest data: ${e.message}")
        }

        try {
            jobQuestSheetService.syncJobQuestProgressData()
            syncMessages.add("JobQuestProgress ")
        } catch (e: Exception) {
            syncErrorMessages.add("Error synchronizing JobQuestProgress data: ${e.message}")
        }

        try {
            expSheetService.syncExpData()
            syncMessages.add("Exp ")
        } catch (e: Exception) {
            syncErrorMessages.add("Error synchronizing exp data: ${e.message}")
        }

        syncMessages.add("] sheet data synchronized successfully. ")
        val okMessage = syncMessages.joinToString("")
        val errorMessage = syncErrorMessages.joinToString("\n")
        val resultMessage = okMessage + errorMessage

        return ResponseEntity.ok(resultMessage)
    }
}
