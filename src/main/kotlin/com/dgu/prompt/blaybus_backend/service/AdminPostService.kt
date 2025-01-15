package com.dgu.prompt.blaybus_backend.service

import com.dgu.prompt.blaybus_backend.data.dto.NotificationRequest
import com.dgu.prompt.blaybus_backend.data.dto.PostRequest
import com.dgu.prompt.blaybus_backend.data.entity.NotificationType
import com.dgu.prompt.blaybus_backend.data.entity.Post
import com.dgu.prompt.blaybus_backend.data.entity.Users
import com.dgu.prompt.blaybus_backend.data.repository.FcmTokenRepository
import com.dgu.prompt.blaybus_backend.data.repository.PostRepository
import com.dgu.prompt.blaybus_backend.data.repository.UsersRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AdminPostService(
    private val postRepository: PostRepository,
    private val userRepository: UsersRepository,
    private val fcmTokenRepository: FcmTokenRepository,
    private val notificationService: NotificationService
) {

    fun createPost(employeeNumber: Int, postRequest: PostRequest) {
        val adminUser = userRepository.findById(employeeNumber)
            .orElseThrow { IllegalArgumentException("User not found") }

        if (!adminUser.isAdmin) {
            throw org.springframework.security.access.AccessDeniedException("Admin privileges required")
        }

        val post = Post(
            postId = 0, // ID는 자동 생성
            postTitle = postRequest.postTitle,
            content = postRequest.content,
            createdAt = LocalDateTime.now(),
            updatedAt = null,
            viewCount = 0 // 초기값
        )
        postRepository.save(post)

        // 모든 유저에게 푸시 알림 발송
        val allUsers = userRepository.findAll() // 모든 유저 조회
        allUsers.forEach { user ->
            val fcmToken = fcmTokenRepository.findByUser(user)?.fcmToken
            if (fcmToken != null) {
                notificationService.sendNotification(
                    NotificationRequest(
                        employeeNumber = user.employeeNumber,
                        type = NotificationType.POST,
                        postTitle = post.postTitle
                    )
                )
            }
        }
    }
        fun getUserByEmployeeNumber(employeeNumber: Int): Users {
            return userRepository.findById(employeeNumber)
                .orElseThrow { IllegalArgumentException("User not found") }
        }
    }

