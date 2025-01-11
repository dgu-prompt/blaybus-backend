package com.dgu.prompt.blaybus_backend.security

import com.dgu.prompt.blaybus_backend.data.entity.Users
import com.dgu.prompt.blaybus_backend.data.repository.UsersRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UsersRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        // username을 기반으로 사용자를 조회
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found")

        return CustomUserDetails(user)
    }
}
