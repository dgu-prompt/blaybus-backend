package com.dgu.prompt.blaybus_backend.service

import com.dgu.prompt.blaybus_backend.data.dto.LoginRequest
import com.dgu.prompt.blaybus_backend.security.JwtUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil
) {

    fun authenticate(loginRequest: LoginRequest): String {
        val authToken = UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        authenticationManager.authenticate(authToken)
        return jwtUtil.generateToken(loginRequest.username)
    }
}