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
        // Users 엔티티에 필요한 다른 필드를 기본값으로 설정하거나 null로 설정한 후 저장
        val user = Users(username = username, password = password) // password 외의 값은 null이 될 수 있음
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