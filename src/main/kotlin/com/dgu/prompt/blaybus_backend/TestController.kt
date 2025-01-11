package com.dgu.prompt.blaybus_backend

import com.dgu.prompt.blaybus_backend.security.JwtUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class TestController(
    private val jwtUtil: JwtUtil // JwtUtil을 통해 JWT를 검증하고 username을 추출
) {

    @GetMapping("/username")
    fun getUsernameFromToken(
        @RequestHeader("Authorization") authHeader: String // 헤더에서 토큰 받아오기
    ): ResponseEntity<String> {
        // Authorization 헤더에서 "Bearer "를 제외한 토큰 부분 추출
        val token = authHeader.takeIf { it.startsWith("Bearer ") }?.substring(7)

        return if (token != null) {
            try {
                // 토큰에서 username 추출
                val username = jwtUtil.extractUsername(token)

                // username이 null이 아니면 성공적으로 반환
                if (username != null) {
                    ResponseEntity.ok(username)
                } else {
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token")
                }
            } catch (e: Exception) {
                // 예외 처리 (예: 토큰이 잘못된 경우)
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token")
            }
        } else {
            // Authorization 헤더가 없으면 오류 반환
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header missing")
        }
    }


    @GetMapping("/test")
    fun testEndpoint(): String {
        return "Hello from Blaybus Backend!"
    }
}