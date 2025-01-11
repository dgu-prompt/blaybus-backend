package com.dgu.prompt.blaybus_backend.controller

import com.dgu.prompt.blaybus_backend.data.dto.LoginRequest
import com.dgu.prompt.blaybus_backend.data.entity.Users
import com.dgu.prompt.blaybus_backend.security.CustomUserDetails
import com.dgu.prompt.blaybus_backend.security.CustomUserDetailsService
import com.dgu.prompt.blaybus_backend.security.JwtUtil
import com.dgu.prompt.blaybus_backend.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val userDetailsService: CustomUserDetailsService
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<*> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )

        SecurityContextHolder.getContext().authentication = authentication

        // 로그인한 사용자의 employeeNumber 가져오기
        val userDetails = SecurityContextHolder.getContext().authentication.principal as CustomUserDetails
        val user = userDetails.getUser() // 이제 Users 객체를 가져올 수 있음
        val employeeNumber = user.employeeNumber // Users 객체에서 employeeNumber 가져오기

        // JWT 토큰 생성 (employeeNumber 포함)
        val token = jwtUtil.generateToken(loginRequest.username, employeeNumber)

        return ResponseEntity.ok(mapOf("token" to token))
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
