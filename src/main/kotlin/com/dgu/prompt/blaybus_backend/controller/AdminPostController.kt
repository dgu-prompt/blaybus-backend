package com.dgu.prompt.blaybus_backend.controller

import com.dgu.prompt.blaybus_backend.data.dto.PostRequest
import com.dgu.prompt.blaybus_backend.security.JwtUtil
import com.dgu.prompt.blaybus_backend.service.AdminPostService
import com.dgu.prompt.blaybus_backend.service.AdminUserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin/posts")
class AdminPostController(
    private val adminPostService: AdminPostService,
    private val jwtUtil: JwtUtil
) {

    @PostMapping
    fun createPost(
        @RequestHeader("Authorization") token: String,
        @RequestBody postRequest: PostRequest
    ): ResponseEntity<String> {
        val employeeNumber = jwtUtil.extractEmployeeNumber(token.replace("Bearer ", ""))
            ?: throw IllegalArgumentException("Invalid token")
        val adminUser = adminPostService.getUserByEmployeeNumber(employeeNumber)
        if (!adminUser.isAdmin) {
            throw org.springframework.security.access.AccessDeniedException("Admin privileges required")
        }

        adminPostService.createPost(employeeNumber, postRequest)
        return ResponseEntity.ok("Post created successfully")
    }
}
