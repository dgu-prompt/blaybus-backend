package com.dgu.prompt.blaybus_backend

import com.dgu.prompt.blaybus_backend.googleSheetsService.*
import org.springframework.boot.CommandLineRunner
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

// 정각마다 시트 동기화 작업을 처리하는 클래스
@Component
class SyncScheduler(
    private val usersSheetService: UsersSheetService,
    private val levelSheetService: LevelSheetService,
    private val postSheetService: PostSheetService,
    private val PRSheetService: PRSheetService,
    private val leaderQuestSheetService: LeaderQuestSheetService,
    private val jobQuestSheetService: JobQuestSheetService,
    private val expSheetService: ExpSheetService,
    private val projectSheetService: ProjectSheetService
) {

    // 게시글과 경험치 관련 데이터는 매일 자정에 동기화
    @Scheduled(cron = "0 0 0 * * *")
    fun syncGoogleSheetsData() {
        postSheetService.syncPosts()
        println("게시글 데이터 동기화가 완료되었습니다.")

//        PRSheetService.syncPRExpData()
//        println("참고. 인사평가 Sheet 동기화가 완료되었습니다.")

        leaderQuestSheetService.syncLeaderQuestData()
        leaderQuestSheetService.syncLeaderQuestProgressData()
        println("리더부여 퀘스트 데이터 동기화가 완료되었습니다.")

        jobQuestSheetService.syncJobQuestData()
        jobQuestSheetService.syncJobQuestProgressData()
        println("직무별 퀘스트 데이터 동기화가 완료되었습니다.")

        expSheetService.syncExpData()
        println("올해 경험치 데이터 동기화가 완료되었습니다.")

        projectSheetService.syncProjectData()
        println("전사 프로젝트 데이터 동기화가 완료되었습니다.")
    }

    // 매월 1일 자정에 한 번만 실행
    @Scheduled(cron = "0 0 0 1 * *")
    fun syncLevelSheetData() {
        usersSheetService.syncData()
        println("유저정보 데이터 동기화가 완료되었습니다.")

        levelSheetService.syncLevels()
        println("레벨 정보 데이터 동기화가 완료되었습니다.")
    }
}

// 애플리케이션 실행 시 즉시 시트를 동기화하는 클래스
@Component
class SyncRunner(
    private val usersSheetService: UsersSheetService,
    private val levelSheetService: LevelSheetService,
    private val postSheetService: PostSheetService,
    private val PRSheetService: PRSheetService,
    private val leaderQuestSheetService: LeaderQuestSheetService,
    private val jobQuestSheetService: JobQuestSheetService,
    private val expSheetService: ExpSheetService,
    private val projectSheetService: ProjectSheetService
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        // 시트 동기화 작업 수행
//        usersSheetService.syncData()
//        println("usersSheet 동기화가 완료되었습니다.")
//
//        levelSheetService.syncLevels()
//        println("levelSheet 동기화가 완료되었습니다.")
//
        postSheetService.syncPosts()
        println("참고. 게시판 Sheet 동기화가 완료되었습니다.")

//        PRSheetService.syncPRExpData()
//        println("참고. 인사평가 Sheet 동기화가 완료되었습니다.")
//
//        leaderQuestSheetService.syncLeaderQuestData()
//        println("리더부여 퀘스트 동기화가 완료되었습니다.")
//
//        leaderQuestSheetService.syncLeaderQuestProgressData()
//        println("LeaderQuestProgressData 동기화가 완료되었습니다.")
//
//        jobQuestSheetService.syncJobQuestData()
//        println("참고. 직무별 퀘스트 동기화가 완료되었습니다.")
//
//        jobQuestSheetService.syncJobQuestProgressData()
//        println("참고. 직무별 퀘스트 동기화가 완료되었습니다.")
//
//        expSheetService.syncExpData()
//        println("expSheet 동기화가 완료되었습니다.")

//        projectSheetService.syncProjectData()
//        println("projectSheet 동기화가 완료되었습니다.")
    }
}
