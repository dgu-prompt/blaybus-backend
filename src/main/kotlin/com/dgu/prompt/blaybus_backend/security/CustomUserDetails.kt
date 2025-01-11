package com.dgu.prompt.blaybus_backend.security

import com.dgu.prompt.blaybus_backend.data.entity.Users
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(private val user: Users) : UserDetails {
    override fun getUsername(): String {
        return user.username ?: throw IllegalArgumentException("Username cannot be null") // username이 null일 경우 예외 처리
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("USER")) // 권한 설정
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    // Users 객체를 반환하는 메소드 추가
    fun getUser(): Users {
        return user
    }
}
