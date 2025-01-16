package com.dgu.prompt.blaybus_backend.controller

import com.dgu.prompt.blaybus_backend.data.dto.NotificationRequest
import com.dgu.prompt.blaybus_backend.security.JwtUtil
import com.dgu.prompt.blaybus_backend.service.NotificationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/notifications")
class NotificationController(
    private val jwtUtil: JwtUtil,
    private val notificationService: NotificationService
) {
    @PostMapping("/fcm/token")
    fun saveFcmToken(
        @RequestHeader("Authorization") token: String,
        @RequestParam fcmToken: String
    ): ResponseEntity<String> {
        val employeeNumber = jwtUtil.extractEmployeeNumber(token.replace("Bearer ", ""))
            ?: throw IllegalArgumentException("Invalid token")
        notificationService.saveFcmToken(employeeNumber, fcmToken)
        return ResponseEntity.ok("FCM Token saved successfully")
    }

    @PostMapping("/notify")
    fun sendNotification(
        @RequestHeader("Authorization") token: String,
        @RequestBody request: NotificationRequest
    ): ResponseEntity<String> {
        val employeeNumber = jwtUtil.extractEmployeeNumber(token.replace("Bearer ", ""))
            ?: throw IllegalArgumentException("Invalid token")
        // Pass the extracted `employeeNumber` to the request object
        val updatedRequest = request.copy(employeeNumber = employeeNumber)
        notificationService.sendNotification(updatedRequest)
        return ResponseEntity.ok("Experience notification sent successfully")
    }

    // 알림 목록 조회
    @GetMapping
    fun getNotifications(@RequestHeader("Authorization") token: String): ResponseEntity<*> {
        val jwtToken = token.replace("Bearer ", "")

        // JWT에서 employeeNumber 추출
        val employeeNumber = jwtUtil.extractEmployeeNumber(jwtToken)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token")

        // employeeNumber를 기반으로 알림 정보 가져오기
        val notifications = notificationService.getNotificationsByEmployeeNumber(employeeNumber)

        return ResponseEntity.ok(notifications)
    }

    // 특정 알림 읽음 처리
    @PutMapping("/{notificationId}/read")
    fun markNotificationAsRead(
        @RequestHeader("Authorization") token: String,
        @PathVariable notificationId: Int
    ): ResponseEntity<Map<String, Any>> {
        val employeeNumber = jwtUtil.extractEmployeeNumber(token.replace("Bearer ", ""))
            ?: throw IllegalArgumentException("Invalid token")
        val result = notificationService.markNotificationAsRead(notificationId)
        return ResponseEntity.ok(result)
    }

    // 모든 알림 읽음 처리
    @PutMapping("/read-all")
    fun markAllNotificationsAsRead(@RequestHeader("Authorization") token: String): ResponseEntity<Map<String, Any>> {
        val employeeNumber = jwtUtil.extractEmployeeNumber(token.replace("Bearer ", ""))
            ?: throw IllegalArgumentException("Invalid token")
        val response = notificationService.markAllNotificationsAsRead(employeeNumber)
        return ResponseEntity.ok(response)
    }
}
