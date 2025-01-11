package com.dgu.prompt.blaybus_backend

import com.dgu.prompt.blaybus_backend.service.GoogleSheetsService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class SyncScheduler(
    private val googleSheetsService: GoogleSheetsService
) {
    @Scheduled(cron = "0 0 * * * *") // 매 시간 정각에 실행
    fun syncGoogleSheetsData() {
        googleSheetsService.syncData()
    }
}
