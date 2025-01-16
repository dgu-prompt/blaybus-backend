package com.dgu.prompt.blaybus_backend.service


import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service

@Service
class FCMService {

    fun sendNotification(fcmToken: String, title: String, body: String) {
        try {
            val message = Message.builder()
                .setToken(fcmToken)
                .setNotification(
                    Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build()
                )
                .putData("title", title)
                .putData("body", body)
                .build()

            FirebaseMessaging.getInstance().send(message)
            println("FCM 알림 전송 성공: $fcmToken")
        } catch (e: FirebaseMessagingException) {
            println("FCM 알림 전송 실패: ${e.message}")
            if (e.message?.contains("registration token is not valid") == true) {
                println("유효하지 않은 FCM 토큰: $fcmToken")
            }
        }
    }
}