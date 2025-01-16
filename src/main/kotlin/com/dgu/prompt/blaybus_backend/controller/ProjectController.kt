package com.dgu.prompt.blaybus_backend.controller

import com.dgu.prompt.blaybus_backend.data.dto.ProjectResponse
import com.dgu.prompt.blaybus_backend.service.ProjectService
import com.dgu.prompt.blaybus_backend.security.JwtUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/projects")
class ProjectController(
    private val jwtUtil: JwtUtil,
    private val projectService: ProjectService
) {
    @GetMapping
    fun getProjects(
        @RequestHeader("Authorization") authToken: String
    ): ResponseEntity<List<ProjectResponse>> {
        val token = authToken.removePrefix("Bearer ")
        val employeeNumber = jwtUtil.extractEmployeeNumber(token)
            ?: throw IllegalArgumentException("Invalid token")

        val projects = projectService.getProjectsByEmployeeNumber(employeeNumber)
        return ResponseEntity.ok(projects)
    }
}
