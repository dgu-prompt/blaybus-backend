package com.dgu.prompt.blaybus_backend.config

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
            // 로그인, 로그아웃과 같은 API는 인증을 요구하지 않으므로 필터를 통과시킴
            filterChain.doFilter(request, response)
            return
        }

        val authHeader = request.getHeader("Authorization")
        val token = authHeader?.takeIf { it.startsWith("Bearer ") }?.substring(7)

        if (token != null) {
            val username = jwtUtil.extractUsername(token)

            if (username != null && jwtUtil.validateToken(token, username)) {
                val userDetails = customUserDetailsService.loadUserByUsername(username)
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )
                SecurityContextHolder.getContext().authentication = authToken
            }
        }
        filterChain.doFilter(request, response)
    }
}