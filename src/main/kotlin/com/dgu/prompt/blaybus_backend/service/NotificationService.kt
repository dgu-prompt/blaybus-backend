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

        // 알림 내용 생성
        val content = when (request.type) {
            NotificationType.EXP -> "${request.points} do 를 획득하셨습니다!"
            NotificationType.POST -> "게시글: ${request.postTitle} 를 확인하세요!"
            NotificationType.SUCCESS -> "${request.period} 목표를 달성하셨습니다!"
        }

        val title = if (request.title.isNotBlank()) request.title else when (request.type) {
            NotificationType.EXP -> "포인트 획득!"
            NotificationType.POST -> "새로운 게시글 알림"
            NotificationType.SUCCESS -> "퀘스트 달성!"
            else -> "알림"
        }

        // 알림 저장
        val notification = Notification(
            users = user,
            content = content,
            type = request.type,
            createdAt = LocalDateTime.now(),
            isRead = false,
            updatedAt = LocalDateTime.now()
        )
        notificationRepository.save(notification)

        // 푸시 알림 전송
        fcmService.sendNotification(
            fcmToken = fcmToken,
            title = title,  // DB 저장된 알림과 동일한 제목 사용
            body = content   // DB 저장된 알림과 동일한 내용 사용
        )
    }

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
