package com.dgu.prompt.blaybus_backend.controller

import com.dgu.prompt.blaybus_backend.service.ExpHistoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import com.dgu.prompt.blaybus_backend.security.JwtUtil
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/exps")
class ExpHistoryController(
    private val expHistoryService: ExpHistoryService,
    private val jwtUtil: JwtUtil
) {

    @GetMapping("/history")
    fun getExpHistory(@RequestHeader("Authorization") token: String): ResponseEntity<Any> {
        val jwtToken = if (token.startsWith("Bearer ")) token.substring(7) else token
        val employeeNumber = jwtUtil.extractEmployeeNumber(jwtToken) 
            ?: throw IllegalArgumentException("Invalid token: employeeNumber not found")
        val expHistory = expHistoryService.getExpHistoryByEmployeeNumber(employeeNumber)
        return ResponseEntity.ok(mapOf("exps_history" to expHistory))
    }
}
