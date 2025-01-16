package com.dgu.prompt.blaybus_backend

import com.dgu.prompt.blaybus_backend.data.dto.NotificationRequest
import com.dgu.prompt.blaybus_backend.data.entity.NotificationType
import com.dgu.prompt.blaybus_backend.data.entity.Post
import com.dgu.prompt.blaybus_backend.data.entity.Users
import com.dgu.prompt.blaybus_backend.data.repository.FcmTokenRepository
import com.dgu.prompt.blaybus_backend.data.repository.UsersRepository
import com.dgu.prompt.blaybus_backend.service.NotificationService

class NotificationManager(
    private val userRepository: UsersRepository,
    private val fcmTokenRepository: FcmTokenRepository,
    private val notificationService: NotificationService
) {

    // 1. 게시글 등록 시 푸시 알림 전송
    fun sendPostNotification(post: Post) {
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

    // 2. 특정 유저가 경험치를 획득했을 때 푸시 알림 전송
    fun sendExpNotification(user: Users, points: Int) {
        val fcmToken = fcmTokenRepository.findByUser(user)?.fcmToken
        if (fcmToken != null) {
            notificationService.sendNotification(
                NotificationRequest(
                    employeeNumber = user.employeeNumber,
                    type = NotificationType.EXP,
                    points = points
                )
            )
        }
    }

    // 3. 주/월별 목표를 달성한 유저들에게 푸시 알림 전송
    fun sendSuccessNotification(users: List<Users>, period: String) {
        users.forEach { user ->
            val fcmToken = fcmTokenRepository.findByUser(user)?.fcmToken
            if (fcmToken != null) {
                notificationService.sendNotification(
                    NotificationRequest(
                        employeeNumber = user.employeeNumber,
                        type = NotificationType.SUCCESS,
                        period = period
                    )
                )
            }
        }
    }
}
