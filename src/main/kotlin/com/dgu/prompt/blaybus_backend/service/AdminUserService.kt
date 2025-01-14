package com.dgu.prompt.blaybus_backend.service;

import com.dgu.prompt.blaybus_backend.data.dto.UserRequest
import com.dgu.prompt.blaybus_backend.data.dto.UserResponse
import com.dgu.prompt.blaybus_backend.data.dto.UserUpdateRequest
import com.dgu.prompt.blaybus_backend.data.entity.Users
import com.dgu.prompt.blaybus_backend.data.repository.UsersRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class UserService(private val userRepository: UsersRepository) {

    fun createUser(adminEmployeeNumber: Int, userRequest: UserRequest) {
        // 관리자 권한 확인
        val adminUser = userRepository.findById(adminEmployeeNumber)
            .orElseThrow { IllegalArgumentException("Admin not found") }

        if (!adminUser.isAdmin) {
            throw AccessDeniedException("Only admins can create accounts")
        }

        // 초기 비밀번호 설정
        val defaultPassword = "1111"

        // 새로운 구성원 계정 생성
        val newUser = Users(
            employeeNumber = userRequest.employeeNumber.toInt(),
            levelId = userRequest.level,
            jobGroupId = userRequest.jobGroupId,
            departmentId = userRequest.department,
            employeeName = userRequest.employeeName,
            username = userRequest.username, // 사용자 ID
            password = defaultPassword, // 기본 패스워드
            joinDate = SimpleDateFormat("yyyy-MM-dd").parse(userRequest.joinDate),
            isAdmin = false, // 일반 사용자
            characterUrl = null, // 기본값
            updatedAt = LocalDateTime.now()
        )

        userRepository.save(newUser)
    }

    fun getAllUsers(): List<UserResponse> {
        return userRepository.findAll().map {
            UserResponse(
                employeeNumber = it.employeeNumber.toString(),
                employeeName = it.employeeName,
                department = it.departmentId,
                joinDate = it.joinDate.toString(),
                level = it.levelId
            )
        }
    }

    fun updateUser(employeeNumber: Int, userUpdateRequest: UserUpdateRequest) {
        val user = userRepository.findById(employeeNumber)
            .orElseThrow { IllegalArgumentException("User not found") }

        userRepository.save(
            user.copy(
                departmentId = userUpdateRequest.department ?: user.departmentId,
                levelId = userUpdateRequest.level ?: user.levelId,
                updatedAt = LocalDateTime.now()
            )
        )
    }

    fun getUserByEmployeeNumber(employeeNumber: Int): Users {
        return userRepository.findById(employeeNumber)
            .orElseThrow { IllegalArgumentException("User not found") }
    }
}

