package com.dgu.prompt.blaybus_backend.config

import com.dgu.prompt.blaybus_backend.security.CustomUserDetails
import com.dgu.prompt.blaybus_backend.security.CustomUserDetailsService
import com.dgu.prompt.blaybus_backend.security.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val customUserDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 인증이 필요한 요청만 처리
        if (request.requestURI.startsWith("/api/auth/")) {
            // 로그인 API는 인증을 요구하지 않으므로 필터를 통과시킴
            filterChain.doFilter(request, response)
            return
        }

        // Authorization 헤더에서 Bearer 토큰을 가져옴
        val authHeader = request.getHeader("Authorization")
        val token = authHeader?.takeIf { it.startsWith("Bearer ") }?.substring(7)

        if (token != null) {
            val username: String = jwtUtil.extractUsername(token) ?: throw IllegalArgumentException("Username not found")
            val employeeNumber = jwtUtil.extractEmployeeNumber(token)

            if (username != null && employeeNumber != null && jwtUtil.validateToken(token, username)) {
                val userDetails = customUserDetailsService.loadUserByUsername(username)
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )

                // CustomUserDetails에서 Users 객체를 가져옴
                val user = (userDetails as CustomUserDetails).getUser()

                // employeeNumber를 인증 정보에 추가
                SecurityContextHolder.getContext().authentication = authToken
            } else {
                response.status = HttpServletResponse.SC_FORBIDDEN
                return
            }
        } else {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }

        filterChain.doFilter(request, response)
    }
}
