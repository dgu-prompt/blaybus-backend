package com.dgu.prompt.blaybus_backend.service

import com.dgu.prompt.blaybus_backend.data.repository.NotificationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository
) {

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
            notification.isRead = true // var로 변경 후 값 수정 가능
        }
        notificationRepository.saveAll(notifications)

        return mapOf(
            "message" to "All notifications marked as read",
            "count" to notifications.size
        )
    }
}
