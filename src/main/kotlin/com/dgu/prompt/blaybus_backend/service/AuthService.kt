package com.dgu.prompt.blaybus_backend.service

import com.dgu.prompt.blaybus_backend.data.dto.LoginRequest
import com.dgu.prompt.blaybus_backend.data.entity.Users
import com.dgu.prompt.blaybus_backend.security.CustomUserDetailsService
import com.dgu.prompt.blaybus_backend.security.JwtUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val userDetailsService: CustomUserDetailsService
) {

    fun authenticate(loginRequest: LoginRequest): String {
        // 인증 토큰 생성
        val authToken = UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)

        // 인증 처리
        val authentication = authenticationManager.authenticate(authToken)

        // 인증된 사용자 정보 가져오기
        val userDetails = userDetailsService.loadUserByUsername(loginRequest.username)

        // employeeNumber 추출 (여기서는 이미 userDetails에서 가져오므로 따로 변환할 필요 없음)
        val employeeNumber = (userDetails as Users).employeeNumber

        // JWT 토큰 생성 (employeeNumber 포함)
        return jwtUtil.generateToken(loginRequest.username, employeeNumber)
    }
}
