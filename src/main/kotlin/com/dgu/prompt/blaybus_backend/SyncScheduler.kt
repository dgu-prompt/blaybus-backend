package com.dgu.prompt.blaybus_backend

import com.dgu.prompt.blaybus_backend.googleSheetsService.LevelSheetService
import com.dgu.prompt.blaybus_backend.googleSheetsService.PostSheetService
import com.dgu.prompt.blaybus_backend.googleSheetsService.UsersSheetService
import org.springframework.boot.CommandLineRunner
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

//@Component
//class SyncScheduler(
//    private val usersSheetService: UsersSheetService
//    private val levelSheetService: LevelSheetService
//)
//{
//    @Scheduled(cron = "0 0 0 * * *") // 매 자정에 실행
//
////    @Scheduled(cron = "0 37 20 * * *") // 오늘 오전 10시 31분에 실행
//    fun syncGoogleSheetsData() {
//        usersSheetService.syncData()
//    }
//}

// 구글 시트 바로 동기화
@Component
class SyncRunner(
    private val usersSheetService: UsersSheetService,
    private val levelSheetService: LevelSheetService,
    private val postSheetService: PostSheetService

) : CommandLineRunner {
    override fun run(vararg args: String?) {
//        println("Google Sheets 동기화를 시작합니다.")
//        levelSheetService.syncLevels()
//        println("Google Sheets 동기화가 완료되었습니다.")

//        println("'참고. 게시판' Sheet 동기화를 시작합니다.")
//        postSheetService.syncPosts()
//        println("'참고. 게시판' Sheet 동기화가 완료되었습니다.")
    }
}
