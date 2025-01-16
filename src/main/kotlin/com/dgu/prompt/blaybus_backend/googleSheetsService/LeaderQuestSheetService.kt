package com.dgu.prompt.blaybus_backend.googleSheetsService

import com.dgu.prompt.blaybus_backend.data.entity.*
import com.dgu.prompt.blaybus_backend.data.repository.*
import com.dgu.prompt.blaybus_backend.service.NotificationService
import com.google.api.services.sheets.v4.Sheets
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class LeaderQuestSheetService(
    private val sheets: Sheets,
    private val leaderQuestRepository: LeaderQuestRepository,
    private val departmentsRepository: DepartmentsRepository,
    private val leaderQuestProgressRepository: LeaderQuestProgressRepository,
    private val usersRepository: UsersRepository,
    private val notificationService: NotificationService,
    private val fcmTokenRepository: FcmTokenRepository
) {
    private val SPREADSHEET_ID = "1gNAIcvtjcarYJ-L9lbzno3pQqmGjdDoItNw9P324Q7c"
    private val LEADERQUESTRANGE = "참고. 리더부여 퀘스트!K11:S"
    private val LEADERQUESTPROGRESSRANGE = "참고. 리더부여 퀘스트!B10:I"
    private val DEPARTMENT_CELL = "참고. 리더부여 퀘스트!K8"

    fun syncLeaderQuestData() {
        try {
            val departmentIdResponse = sheets.spreadsheets().values().get(SPREADSHEET_ID, DEPARTMENT_CELL).execute()
            val departmentId = departmentIdResponse.getValues().flatten().firstOrNull()?.toString()
                ?: throw Exception("departmentId 데이터를 가져올 수 없습니다.")

            val department = departmentsRepository.findById(departmentId).orElseThrow {
                Exception("부서 ID $departmentId 에 해당하는 부서를 찾을 수 없습니다.")
            }

            val response = sheets.spreadsheets().values().get(SPREADSHEET_ID, LEADERQUESTRANGE).execute()
            val values = response.getValues()

            if (!values.isNullOrEmpty()) {
                val leaderQuestsFromSheet = values.mapNotNull { row ->
                    try {
                        val questTitle = row[0]?.toString() ?: throw Exception("퀘스트 제목 누락")
                        val frequencyType = if (row[1]?.toString() == "월") FrequencyType.MONTH else FrequencyType.WEEK
                        val maxExpDo = row[4]?.toString()?.toInt() ?: 0
                        val medianExpDo = row[5]?.toString()?.toInt() ?: 0
                        val maxCondition = row[6]?.toString()
                        val medianCondition = row[7]?.toString()
                        val description = row.getOrNull(8)?.toString() ?: ""

                        LeaderQuest(
                            questId = 0,
                            departments = department,
                            questTitle = questTitle,
                            description = description,
                            maxCondition = maxCondition,
                            medianCondition = medianCondition,
                            maxExpDo = maxExpDo,
                            medianExpDo = medianExpDo,
                            updatedAt = LocalDateTime.now(),
                            frequencyType = frequencyType
                        )
                    } catch (e: Exception) {
                        println("LeaderQuest 데이터 변환 중 오류 발생: ${e.message}")
                        null
                    }
                }

                val existingLeaderQuests =
                    leaderQuestRepository.findAll().associateBy { it.questTitle to it.frequencyType }

                val leaderQuestsToUpdate = leaderQuestsFromSheet.filter { newQuest ->
                    val existingQuest = existingLeaderQuests[newQuest.questTitle to newQuest.frequencyType]
                    if (existingQuest != null) {
                        existingQuest.departments != newQuest.departments ||
                                existingQuest.description != newQuest.description ||
                                existingQuest.maxCondition != newQuest.maxCondition ||
                                existingQuest.medianCondition != newQuest.medianCondition ||
                                existingQuest.maxExpDo != newQuest.maxExpDo ||
                                existingQuest.medianExpDo != newQuest.medianExpDo
                    } else {
                        true
                    }
                }.map { newQuest ->
                    val existingQuest = existingLeaderQuests[newQuest.questTitle to newQuest.frequencyType]
                    if (existingQuest != null) {
                        existingQuest.copy(
                            departments = newQuest.departments,
                            description = newQuest.description,
                            maxCondition = newQuest.maxCondition,
                            medianCondition = newQuest.medianCondition,
                            maxExpDo = newQuest.maxExpDo,
                            medianExpDo = newQuest.medianExpDo,
                            updatedAt = LocalDateTime.now()
                        )
                    } else {
                        newQuest
                    }
                }

                if (leaderQuestsToUpdate.isNotEmpty()) {
                    leaderQuestRepository.saveAll(leaderQuestsToUpdate)
                    println("리더 퀘스트 Sheet 데이터를 동기화했습니다. 업데이트된 항목 수: ${leaderQuestsToUpdate.size}개.")
                } else {
                    println("변경된 데이터가 없습니다.")
                }
            } else {
                println("리더 퀘스트 Sheet에서 데이터를 찾을 수 없습니다.")
            }
        } catch (e: Exception) {
            println("리더 퀘스트 Sheet 동기화 중 오류 발생: ${e.message}")
        }
    }

    fun syncLeaderQuestProgressData() {
        try {
            val departmentIdResponse = sheets.spreadsheets().values().get(SPREADSHEET_ID, DEPARTMENT_CELL).execute()
            val departmentId = departmentIdResponse.getValues().flatten().firstOrNull()?.toString()
                ?: throw Exception("departmentId 데이터를 가져올 수 없습니다.")

            val department = departmentsRepository.findById(departmentId).orElseThrow {
                Exception("부서 ID $departmentId 에 해당하는 부서를 찾을 수 없습니다.")
            }

            val response = sheets.spreadsheets().values().get(SPREADSHEET_ID, LEADERQUESTPROGRESSRANGE).execute()
            val values = response.getValues()

            if (!values.isNullOrEmpty()) {
                val currentTime = LocalDateTime.now()
                val existingProgressMap = leaderQuestProgressRepository.findAll().associateBy { progress ->
                    Triple(progress.leaderQuest.questId, progress.user.employeeNumber, progress.period)
                }

                val progressListToUpdateOrInsert = values.mapNotNull { row ->
                    try {
                        val employeeNumber = row[2].toString().toInt()
                        val questTitle = row[4].toString()
                        val statusString = row[5].toString()
                        val description = row.getOrNull(7)?.toString() ?: ""

                        val frequencyType = when {
                            row.getOrNull(0)?.toString()?.isNotEmpty() == true -> FrequencyType.WEEK
                            row.getOrNull(1)?.toString()?.isNotEmpty() == true -> FrequencyType.MONTH
                            else -> throw IllegalArgumentException("Frequency type이 누락되었습니다.")
                        }

                        val period = when (frequencyType) {
                            FrequencyType.WEEK -> row[0].toString().toInt()
                            FrequencyType.MONTH -> row[1].toString().toInt()
                        }

                        val status = when (statusString) {
                            "Max" -> ProgressStatus.MAX
                            "Median" -> ProgressStatus.MEDIUM
                            else -> throw IllegalArgumentException("Status 변환 오류: $statusString")
                        }

                        val leaderQuest = leaderQuestRepository.findByDepartmentsAndQuestTitleAndFrequencyType(
                            departments = department,
                            questTitle = questTitle,
                            frequencyType = frequencyType
                        ).orElseThrow {
                            Exception("LeaderQuest 객체를 찾을 수 없습니다. 부서 ID: ${department.departmentId}, QuestTitle: $questTitle, FrequencyType: $frequencyType")
                        }

                        val user = usersRepository.findOptionalByEmployeeNumber(employeeNumber).orElseThrow {
                            Exception("User 객체를 찾을 수 없습니다. 사번: $employeeNumber")
                        }

                        val existingProgress = existingProgressMap[Triple(leaderQuest.questId, employeeNumber, period)]

                        if (existingProgress == null) {
                            LeaderQuestProgress(
                                questProgressId = 0,
                                leaderQuest = leaderQuest,
                                user = user,
                                status = status,
                                updatedAt = currentTime,
                                period = period,
                                frequencyType = frequencyType,
                                description = description
                            )
                        } else {
                            null
                        }
                    } catch (e: Exception) {
                        println("데이터 변환 중 오류 발생: ${e.message}")
                        null
                    }
                }.filterNotNull()

                if (progressListToUpdateOrInsert.isNotEmpty()) {
                    leaderQuestProgressRepository.saveAll(progressListToUpdateOrInsert)
                    println("리더 퀘스트 진행 현황 데이터를 동기화했습니다. 업데이트된 항목 수: ${progressListToUpdateOrInsert.size}")

                    // 알림 전송
                    progressListToUpdateOrInsert.forEach { progress ->
                        val points = when (progress.status) {
                            ProgressStatus.MAX -> progress.leaderQuest.maxExpDo
                            ProgressStatus.MEDIUM -> progress.leaderQuest.medianExpDo
                            else -> 0
                        }
                        val user = progress.user
                        if (points > 0) {
                            val fcmToken = fcmTokenRepository.findByUser(user)?.fcmToken
                            if (fcmToken != null) {
                                notificationService.sendNotification(
                                    NotificationRequest(
                                        employeeNumber = user.employeeNumber,
                                        type = NotificationType.SUCCESS,
                                        period = progress.period.toString()
                                    )
                                )
                            }
                        }
                    }
                } else {
                    println("새로 추가된 데이터가 없습니다.")
                }
            } else {
                println("리더 퀘스트 진행 현황 Sheet에서 데이터를 찾을 수 없습니다.")
            }
        } catch (e: Exception) {
            println("리더 퀘스트 진행 현황 동기화 중 오류 발생: ${e.message}")
        }
    }
}
