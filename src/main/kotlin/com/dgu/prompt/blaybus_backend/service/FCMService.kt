package com.dgu.prompt.blaybus_backend.service


import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import org.springframework.stereotype.Service

@Service
class FCMService {

    fun sendNotification(fcmToken: String, title: String, body: String) {
        val message = Message.builder()
            .setToken(fcmToken)
            .putData("title", title)
            .putData("body", body)
            .build()

        FirebaseMessaging.getInstance().send(message)
    }
}