package com.dgu.prompt.blaybus_backend

import com.dgu.prompt.blaybus_backend.googleSheetsService.UsersSheetsService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class SyncScheduler(
    private val usersSheetsService: UsersSheetsService
) {
    @Scheduled(cron = "0 0 0 * * *") // 매 자정에 실행

//    @Scheduled(cron = "0 37 20 * * *") // 오늘 오전 10시 31분에 실행
    fun syncGoogleSheetsData() {
        usersSheetsService.syncData()
    }
}
//구글 시트 바로 동기화
//
//@Component
//class SyncRunner(
//    private val googleSheetsService: GoogleSheetsService
//) : CommandLineRunner {
//    override fun run(vararg args: String?) {
//        println("Google Sheets 동기화를 시작합니다.")
//        googleSheetsService.syncData()
//        println("Google Sheets 동기화가 완료되었습니다.")
//    }
//}
