package com.dgu.prompt.blaybus_backend.service

import com.dgu.prompt.blaybus_backend.data.dto.UserSelfResponse
import com.dgu.prompt.blaybus_backend.data.dto.UserSelfUpdateRequest
import com.dgu.prompt.blaybus_backend.data.repository.UsersRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UsersRepository
) {

    // 특정 유저 정보 조회
    fun getUserByEmployeeNumber(employeeNumber: Int): UserSelfResponse {
        val user = userRepository.findById(employeeNumber)
            .orElseThrow { IllegalArgumentException("User with employeeNumber $employeeNumber not found") }

        return UserSelfResponse(
            employeeNumber = user.employeeNumber.toString(),
            employeeName = user.employeeName,
            username = user.username.toString(),
            department = user.departmentId,
            joinDate = user.joinDate.toString(),
            level = user.levelId,
            password = user.password,
            jobGroupId = user.jobGroupId,
            characterUrl = user.characterUrl
        )
    }

    // 특정 사용자 정보 업데이트
    fun updateUser(employeeNumber: Int, userUpdateRequest: UserSelfUpdateRequest) {
        val user = userRepository.findById(employeeNumber)
            .orElseThrow { IllegalArgumentException("User with employeeNumber $employeeNumber not found") }

        userRepository.save(
            user.copy(
                password = userUpdateRequest.password ?: user.password,
                characterUrl = userUpdateRequest.character ?: user.characterUrl
            )
        )
    }
}
