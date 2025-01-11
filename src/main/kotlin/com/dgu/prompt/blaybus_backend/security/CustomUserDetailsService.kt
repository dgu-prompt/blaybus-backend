package com.dgu.prompt.blaybus_backend.security

import com.dgu.prompt.blaybus_backend.data.repository.UsersRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val usersRepository: UsersRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = usersRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found with username: $username")

        return User(user.username, user.password, listOf())
    }
}