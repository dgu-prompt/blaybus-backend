package com.dgu.prompt.blaybus_backend.controller

import com.dgu.prompt.blaybus_backend.data.dto.UserRequest
import com.dgu.prompt.blaybus_backend.data.dto.UserResponse
import com.dgu.prompt.blaybus_backend.data.dto.UserUpdateRequest
import com.dgu.prompt.blaybus_backend.security.JwtUtil
import com.dgu.prompt.blaybus_backend.service.AdminUserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin/users")
class AdminUserController(
    private val adminUserService: AdminUserService,
    private val jwtUtil: JwtUtil
) {

    @PostMapping
    fun createUser(
        @RequestHeader("Authorization") token: String,
        @RequestBody userRequest: UserRequest
    ): ResponseEntity<String> {
        val employeeNumber = jwtUtil.extractEmployeeNumber(token.replace("Bearer ", ""))
            ?: throw IllegalArgumentException("Invalid token")
        val adminUser = adminUserService.getUserByEmployeeNumber(employeeNumber)
        if (!adminUser.isAdmin) {
            throw org.springframework.security.access.AccessDeniedException("Admin privileges required")
        }

        adminUserService.createUser(userRequest)
        return ResponseEntity.ok("User created successfully")
    }

    @GetMapping
    fun getUsers(
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<List<UserResponse>> {
        val employeeNumber = jwtUtil.extractEmployeeNumber(token.replace("Bearer ", ""))
            ?: throw IllegalArgumentException("Invalid token")
        val adminUser = adminUserService.getUserByEmployeeNumber(employeeNumber)
        if (!adminUser.isAdmin) {
            throw org.springframework.security.access.AccessDeniedException("Admin privileges required")
        }

        return ResponseEntity.ok(adminUserService.getAllUsers())
    }

    @GetMapping("/{employeeNumber}")
    fun getUser(
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<List<UserResponse>> {
        val employeeNumber = jwtUtil.extractEmployeeNumber(token.replace("Bearer ", ""))
            ?: throw IllegalArgumentException("Invalid token")
        val adminUser = adminUserService.getUserByEmployeeNumber(employeeNumber)
        if (!adminUser.isAdmin) {
            throw org.springframework.security.access.AccessDeniedException("Admin privileges required")
        }

        return ResponseEntity.ok(adminUserService.getUser())
    }

    @PutMapping("/{employeeNumber}")
    fun updateUser(
        @RequestHeader("Authorization") token: String,
        @PathVariable employeeNumber: Int,
        @RequestBody userUpdateRequest: UserUpdateRequest
    ): ResponseEntity<String> {
        val adminEmployeeNumber = jwtUtil.extractEmployeeNumber(token.replace("Bearer ", ""))
            ?: throw IllegalArgumentException("Invalid token")
        val adminUser = adminUserService.getUserByEmployeeNumber(adminEmployeeNumber)
        if (!adminUser.isAdmin) {
            throw org.springframework.security.access.AccessDeniedException("Admin privileges required")
        }

        adminUserService.updateUser(employeeNumber, userUpdateRequest)
        return ResponseEntity.ok("User information updated")
    }
}
