package com.dgu.prompt.blaybus_backend.security

import org.springframework.stereotype.Service

@Service
class CustomAuthenticationService(
    private val customUserDetailsService: CustomUserDetailsService
) {

    // 로그인 시 입력된 비밀번호와 DB에 저장된 비밀번호를 평문 그대로 비교
    fun authenticate(username: String, password: String): Boolean {
        val user = customUserDetailsService.loadUserByUsername(username)

        return password == user.password
    }
}
