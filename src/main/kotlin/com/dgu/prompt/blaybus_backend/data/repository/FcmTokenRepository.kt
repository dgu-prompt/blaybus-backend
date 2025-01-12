package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.FcmToken
import com.dgu.prompt.blaybus_backend.data.entity.Users
import org.springframework.data.jpa.repository.JpaRepository

interface FcmTokenRepository : JpaRepository<FcmToken, Long> {
    fun findByUser(user: Users): FcmToken?
}
