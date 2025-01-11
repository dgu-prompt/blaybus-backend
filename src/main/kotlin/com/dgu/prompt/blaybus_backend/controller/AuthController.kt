package com.dgu.prompt.blaybus_backend.controller

import com.dgu.prompt.blaybus_backend.data.dto.LoginRequest
import com.dgu.prompt.blaybus_backend.security.JwtUtil
import com.dgu.prompt.blaybus_backend.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        val token = authService.authenticate(loginRequest)
        return ResponseEntity.ok(mapOf("data" to mapOf("token" to token, "expires_in" to jwtUtil.expiration)))
    }

    @PostMapping("/logout")
    fun logout(@RequestHeader("Authorization") token: String): ResponseEntity<Any> {
        val jwtToken = token.replace("Bearer ", "")
        return if (jwtUtil.invalidateToken(jwtToken)) {
            ResponseEntity.ok("로그아웃 성공")
        } else {
            ResponseEntity.status(400).body("로그아웃 실패")
        }
    }
}