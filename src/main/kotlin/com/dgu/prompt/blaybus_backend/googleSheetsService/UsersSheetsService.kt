package com.dgu.prompt.blaybus_backend.googleSheetsService

import com.dgu.prompt.blaybus_backend.data.entity.Users2
import com.dgu.prompt.blaybus_backend.data.repository.Users2Repository
import com.google.api.services.sheets.v4.Sheets
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class UsersSheetsService(
    private val sheets: Sheets,
    private val users2Repository: Users2Repository
) {
    //    private val SPREADSHEET_ID = "GoogleSheets" // Google Sheets ID
    private val SPREADSHEET_ID = "1gNAIcvtjcarYJ-L9lbzno3pQqmGjdDoItNw9P324Q7c" // Google Sheets ID
    private val RANGE = "참고. 구성원 정보!B9:J" // 데이터 범위 (헤더 제외)
    fun syncData() {
        try {
            val response = sheets.spreadsheets().values().get(SPREADSHEET_ID, RANGE).execute()
            val values = response.getValues()

            if (!values.isNullOrEmpty()) {
                val usersList = values.mapNotNull { row ->
                    try {
                        Users2(
                            employeeNumber = row[0].toString().toInt(),
                            employeeName = row[1].toString(),
                            joinDate = LocalDate.parse(row[2].toString(), DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay(ZoneId.systemDefault()).toInstant().let { Date.from(it) },
                            departmentId = row[3].toString(),
                            jobGroupId = row[4].toString().toInt(),
                            levelId = row[5].toString(),
                            username = row[6]?.toString(),
                            password = row[7]?.toString() ?: "",
                            isAdmin = false,
                            updatedAt = LocalDateTime.now()
                        )
                    } catch (e: Exception) {
                        println("데이터 변환 중 오류 발생: ${e.message}")
                        null
                    }
                }

                // 중복 제거 및 저장
                val existingUsers = users2Repository.findAllById(usersList.map { it.employeeNumber })
                val newUsers = usersList.filter { user -> existingUsers.none { it.employeeNumber == user.employeeNumber } }

                if (newUsers.isNotEmpty()) {
                    users2Repository.saveAll(newUsers)
                    println("새로운 사용자 ${newUsers.size}명을 저장했습니다.")
                } else {
                    println("저장할 새로운 사용자가 없습니다.")
                }
            } else {
                println("Google Sheets에서 데이터를 찾을 수 없습니다.")
            }
        } catch (e: Exception) {
            println("Google Sheets 데이터 동기화 중 오류 발생: ${e.message}")
        }
    }
}