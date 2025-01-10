package com.dgu.prompt.blaybus_backend.controller

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): LoginResponse {
        return LoginResponse(
            token = "JWT_TOKEN",
            expiresIn = 3600
        )
    }

    @PostMapping("/logout")
    fun logout(@RequestHeader("Authorization") token: String): Map<String, String> {
        return mapOf("status" to "Logout successful")
    }
}

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val expiresIn: Int
)