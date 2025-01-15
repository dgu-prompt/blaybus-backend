package com.dgu.prompt.blaybus_backend.service

import com.dgu.prompt.blaybus_backend.data.dto.NotificationRequest
import com.dgu.prompt.blaybus_backend.data.entity.FcmToken
import com.dgu.prompt.blaybus_backend.data.entity.Notification
import com.dgu.prompt.blaybus_backend.data.entity.NotificationType
import com.dgu.prompt.blaybus_backend.data.repository.FcmTokenRepository
import com.dgu.prompt.blaybus_backend.data.repository.NotificationRepository
import com.dgu.prompt.blaybus_backend.data.repository.UsersRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class NotificationService(
    private val fcmService: FCMService,
    private val notificationRepository: NotificationRepository,
    private val usersRepository: UsersRepository,
    private val fcmTokenRepository: FcmTokenRepository
) {
    fun saveFcmToken(employeeNumber: Int, fcmToken: String) {
        val user = usersRepository.findById(employeeNumber)
            .orElseThrow { IllegalArgumentException("User not found") }

        val existingToken = fcmTokenRepository.findByUser(user)

        if (existingToken != null) {
            // 기존 토큰 업데이트
            val updatedToken = existingToken.copy(fcmToken = fcmToken, createdAt = LocalDateTime.now())
            fcmTokenRepository.save(updatedToken)
        } else {
            // 새 토큰 저장
            val newToken = FcmToken(user = user, fcmToken = fcmToken)
            fcmTokenRepository.save(newToken)
        }
    }

    fun sendNotification(request: NotificationRequest) {
        val user = usersRepository.findById(request.employeeNumber)
            .orElseThrow { IllegalArgumentException("User not found") }

        val fcmToken = fcmTokenRepository.findByUser(user)?.fcmToken
            ?: throw IllegalArgumentException("FCM token not found for user")

        val content = when (request.type) {
            NotificationType.EXP -> "${request.points} 두를 획득하셨습니다!"
            NotificationType.POST -> "새 게시글: ${request.postTitle} 글이 올라왔어요!"
            NotificationType.SUCCESS -> "${request.period} 목표를 달성하셨습니다!"
        }

        // Create and save notification entity
        val notification = Notification(
            users = user,
            content = content,
            type = request.type,
            createdAt = LocalDateTime.now(),
            isRead = false,
            updatedAt = LocalDateTime.now()
        )
        notificationRepository.save(notification)

        // Send push notification via FCM
        fcmService.sendNotification(
            fcmToken = fcmToken,
            title = request.title,
            body = content
        )
    }
/*
    fun sendExperienceNotification(employeeNumber: Int, points: Int) {
        val user = usersRepository.findById(employeeNumber)
            .orElseThrow { IllegalArgumentException("User not found") }

        val fcmToken = fcmTokenRepository.findByUser(user)?.fcmToken
            ?: throw IllegalArgumentException("FCM token not found for user")

        val content = "You have gained $points experience points!"
        fcmService.sendNotification(fcmToken, "Experience Gained", content)

        val notification = Notification(
            notificationId = 0, // Auto-generated
            users = user,
            content = content,
            type = NotificationType.EXP,
            createdAt = LocalDateTime.now(),
            isRead = false,
            updatedAt = LocalDateTime.now()
        )
        notificationRepository.save(notification)
    }

    fun sendPostNotification(employeeNumber: Int, postTitle: String) {
        val user = usersRepository.findById(employeeNumber)
            .orElseThrow { IllegalArgumentException("User not found") }

        val fcmToken = fcmTokenRepository.findByUser(user)?.fcmToken
            ?: throw IllegalArgumentException("FCM token not found for user")

        val content = "A new post titled '$postTitle' has been published."
        fcmService.sendNotification(fcmToken, "New Post", content)

        val notification = Notification(
            notificationId = 0,
            users = user,
            content = content,
            type = NotificationType.POST,
            createdAt = LocalDateTime.now(),
            isRead = false,
            updatedAt = LocalDateTime.now()
        )
        notificationRepository.save(notification)
    }

    fun sendAchievementNotification(employeeNumber: Int, period: String) {
        val user = usersRepository.findById(employeeNumber)
            .orElseThrow { IllegalArgumentException("User not found") }

        val fcmToken = fcmTokenRepository.findByUser(user)?.fcmToken
            ?: throw IllegalArgumentException("FCM token not found for user")

        val content = "Congratulations on your $period achievements!"
        fcmService.sendNotification(fcmToken, "Achievement Unlocked", content)

        val notification = Notification(
            notificationId = 0,
            users = user,
            content = content,
            type = NotificationType.SUCCESS,
            createdAt = LocalDateTime.now(),
            isRead = false,
            updatedAt = LocalDateTime.now()
        )
        notificationRepository.save(notification)
    }*/

    // 알림 목록 조회
    fun getNotificationsByEmployeeNumber(employeeNumber: Int): List<Map<String, Any>> { // Any?로 nullable 처리
        val notifications = notificationRepository.findByUsers_EmployeeNumberOrderByCreatedAtDesc(employeeNumber)
        return notifications.map { notification ->
            mapOf(
                "notificationId" to notification.notificationId,
                "content" to notification.content,
                "updated_at" to notification.updatedAt,
                "type" to notification.type.name.lowercase(),
                "created_at" to (notification.createdAt ?: ""), // 기본값 처리
                "is_read" to notification.isRead
            )
        }
    }

    // 특정 알림 읽음 처리
    @Transactional
    fun markNotificationAsRead(notificationId: Int): Map<String, Any> {
        val notification = notificationRepository.findById(notificationId)
            .orElseThrow { NoSuchElementException("Notification not found") }
        if (!notification.isRead) {
            notification.isRead = true
            notificationRepository.save(notification)
        }
        return mapOf(
            "message" to "Notification marked as read",
            "notification_id" to notificationId
        )
    }

    // 모든 알림 읽음 처리
    @Transactional
    fun markAllNotificationsAsRead(employeeNumber: Int): Map<String, Any> {
        val notifications = notificationRepository.findByUsers_EmployeeNumberAndIsReadFalse(employeeNumber)
        notifications.forEach { notification ->
            notification.isRead = true
        }
        notificationRepository.saveAll(notifications)

        return mapOf(
            "message" to "All notifications marked as read",
            "count" to notifications.size
        )
    }
}
