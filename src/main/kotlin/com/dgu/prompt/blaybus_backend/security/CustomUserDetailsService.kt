package com.dgu.prompt.blaybus_backend.security

import com.dgu.prompt.blaybus_backend.data.entity.Users
import com.dgu.prompt.blaybus_backend.data.repository.UsersRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val usersRepository: UsersRepository) : UserDetailsService {

    // 회원 가입 시 비밀번호를 평문 그대로 저장
    fun saveUser(username: String, password: String) {
        val user = Users(username = username, password = password)
        usersRepository.save(user)
    }


    override fun loadUserByUsername(username: String): UserDetails {
        val user = usersRepository.findByUsername(username)
        if (user == null) throw UsernameNotFoundException("User not found")

        return User(
            user.username,
            user.password, // 평문 비밀번호 그대로 반환
            emptyList()
        )
    }
}