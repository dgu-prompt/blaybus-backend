package com.dgu.prompt.blaybus_backend.googleSheetsService

import com.dgu.prompt.blaybus_backend.data.dto.NotificationRequest
import com.dgu.prompt.blaybus_backend.data.entity.Exp
import com.dgu.prompt.blaybus_backend.data.entity.ExpType
import com.dgu.prompt.blaybus_backend.data.entity.NotificationType
import com.dgu.prompt.blaybus_backend.data.entity.ProgressStatus
import com.dgu.prompt.blaybus_backend.data.repository.ExpRepository
import com.dgu.prompt.blaybus_backend.data.repository.FcmTokenRepository
import com.dgu.prompt.blaybus_backend.data.repository.UsersRepository
import com.dgu.prompt.blaybus_backend.service.NotificationService
import com.google.api.services.sheets.v4.Sheets
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ExpSheetService(
    private val sheets: Sheets,
    private val expRepository: ExpRepository,
    private val usersRepository: UsersRepository,
    private val notificationService: NotificationService,
    private val fcmTokenRepository: FcmTokenRepository
) {
    private val SPREADSHEET_ID = "1gNAIcvtjcarYJ-L9lbzno3pQqmGjdDoItNw9P324Q7c" // Google Sheets ID
    private val RANGE = "참고. 올해 경험치!B26:L" // 데이터 범위

    fun syncExpData() {
        try {
            // 구글 시트 데이터 가져오기
            val response = sheets.spreadsheets().values().get(SPREADSHEET_ID, RANGE).execute()
            val values = response.getValues()

            if (!values.isNullOrEmpty()) {
                val currentYear = 2024
                val expDataFromSheet = values.mapNotNull { row ->
                    try {
                        val employeeNumber = row[0]?.toString()?.toInt()
                        val expDoList = listOf(
                            row[6]?.toString()?.replace(",", "")?.toIntOrNull() ?: 0, // HR_FIRST
                            row[7]?.toString()?.replace(",", "")?.toIntOrNull() ?: 0, // HR_SECOND
                            row[8]?.toString()?.replace(",", "")?.toIntOrNull() ?: 0, // JOB_QUEST
                            row[9]?.toString()?.replace(",", "")?.toIntOrNull() ?: 0, // LEADER_QUEST
                            row[10]?.toString()?.replace(",", "")?.toIntOrNull() ?: 0 // PROJECT
                        )

                        employeeNumber to expDoList
                    } catch (e: Exception) {
                        println("Exp 데이터 변환 중 오류 발생: ${e.message}")
                        null
                    }
                }

                // 기존 데이터에서 expYear가 currentYear인 데이터만 필터링
                val existingExpData = expRepository.findAll()
                    .filter { it.expYear == currentYear }
                    .groupBy { it.employeeNumber to it.expType }

                val usersMap = usersRepository.findAll().associateBy { it.employeeNumber }

                val expToUpdateOrCreate = expDataFromSheet.flatMap { (employeeNumber, expDoList) ->
                    val user = usersMap[employeeNumber]
                    if (user == null) {
                        println("경고: employeeNumber $employeeNumber 에 해당하는 사용자를 찾을 수 없어 데이터를 무시합니다.")
                        return@flatMap emptyList<Exp>() // 해당 데이터를 건너뜁니다.
                    }

                    // 각 expType에 대해 기존 데이터와 비교하여 업데이트하거나 새로운 데이터 추가
                    listOf(
                        ExpType.HR_FIRST,
                        ExpType.HR_SECOND,
                        ExpType.JOB_QUEST,
                        ExpType.LEADER_QUEST,
                        ExpType.PROJECT
                    ).mapIndexed { index, expType ->
                        val newExp = Exp(
                            employeeNumber = user.employeeNumber,
                            expYear = currentYear,
                            expDo = expDoList[index],
                            expType = expType,
                            updatedAt = LocalDateTime.now()
                        )

                        val existingExp = existingExpData[employeeNumber to expType]?.firstOrNull()
                        if (existingExp != null) {
                            // 기존 데이터가 있다면 비교 후 업데이트 결정
                            if (existingExp.expDo != newExp.expDo) {
                                existingExp.copy(
                                    expDo = newExp.expDo,
                                    updatedAt = LocalDateTime.now()
                                )
                            } else {
                                null // 변경 사항 없으면 무시
                            }
                        } else {
                            // 새로운 데이터라면 그대로 추가
                            newExp
                        }
                    }.filterNotNull() // null 제거
                }

                if (expToUpdateOrCreate.isNotEmpty()) {
                    expRepository.saveAll(expToUpdateOrCreate)
                    println("경험치 데이터를 동기화했습니다. 업데이트된 항목 수: ${expToUpdateOrCreate.size}개.")

                    expToUpdateOrCreate.forEach { progress ->
                        val user = usersMap[progress.employeeNumber]
                        val points = progress.expDo // points 계산

                        if (points > 0 && user != null) {
                            val fcmToken = fcmTokenRepository.findByUser(user)?.fcmToken
                            if (fcmToken != null) {
                                notificationService.sendNotification(
                                    NotificationRequest(
                                        employeeNumber = user.employeeNumber,
                                        type = NotificationType.EXP,
                                        points = points
                                    )
                                )
                            }
                        }
                    }
                } else {
                    println("변경된 데이터가 없습니다.")
                }
            } else {
                println("경험치 시트에서 데이터를 찾을 수 없습니다.")
            }
        } catch (e: Exception) {
            println("경험치 데이터 동기화 중 오류 발생: ${e.message}")
        }
    }
}
