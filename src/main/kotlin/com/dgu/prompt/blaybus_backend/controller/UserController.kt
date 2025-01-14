package com.dgu.prompt.blaybus_backend.controller;

import com.dgu.prompt.blaybus_backend.data.dto.UserSelfResponse
import com.dgu.prompt.blaybus_backend.data.dto.UserSelfUpdateRequest
import com.dgu.prompt.blaybus_backend.security.JwtUtil
import com.dgu.prompt.blaybus_backend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val jwtUtil: JwtUtil
) {

    // 회원 개인 페이지 조회
    @GetMapping
    fun getMyInfo(
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<UserSelfResponse> {
        val employeeNumber = jwtUtil.extractEmployeeNumber(token.replace("Bearer ", ""))
            ?: throw IllegalArgumentException("Invalid token")
        val userResponse = userService.getUserByEmployeeNumber(employeeNumber)
        return ResponseEntity.ok(userResponse)
    }

    // 회원 정보 수정
    @PutMapping
    fun updateMyInfo(
        @RequestHeader("Authorization") token: String,
        @RequestBody userUpdateRequest: UserSelfUpdateRequest
    ): ResponseEntity<Map<String, String>> {
        val employeeNumber = jwtUtil.extractEmployeeNumber(token.replace("Bearer ", ""))
            ?: throw IllegalArgumentException("Invalid token")
        userService.updateUser(employeeNumber, userUpdateRequest)
        return ResponseEntity.ok(
            mapOf(
                "status" to "success",
                "message" to "User information updated"
            )
        )
    }
}
