package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.Notification
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationRepository : JpaRepository<Notification, Int> {
    fun findByUsers_EmployeeNumberOrderByCreatedAtDesc(employeeNumber: Int): List<Notification>
    fun findByUsers_EmployeeNumberAndIsReadFalse(employeeNumber: Int): List<Notification>
    fun getNotificationByUsers_EmployeeNumber(employeeNumber: Int): Notification?
}
