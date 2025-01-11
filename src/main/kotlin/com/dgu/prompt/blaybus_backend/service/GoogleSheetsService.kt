package com.dgu.prompt.blaybus_backend.service

import com.dgu.prompt.blaybus_backend.data.entity.Users
import com.dgu.prompt.blaybus_backend.data.repository.UsersRepository
import com.google.api.services.sheets.v4.Sheets
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class GoogleSheetsService(
    private val sheets: Sheets,
    private val usersRepository: UsersRepository
) {
    private val SPREADSHEET_ID = "your-spreadsheet-id" // Google Sheets ID
    private val RANGE = "Sheet1!A2:N" // 데이터 범위 (헤더 제외)

    fun syncData() {
        val response = sheets.spreadsheets().values().get(SPREADSHEET_ID, RANGE).execute()
        val values = response.getValues()

        if (!values.isNullOrEmpty()) {
            val users = values.mapNotNull { row ->
                try {
                    Users(
                        employeeNumber = row[0].toString().toInt(),
                        employeeName = row[1].toString(),
                        joinDate = LocalDate.parse(row[2].toString(), DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay(ZoneId.systemDefault()).toInstant().let { Date.from(it) },
                        departmentId = row[3].toString(),
                        jobGroupId = row[4].toString().toInt(),
                        levelId = row[5].toString(),
                        username = row[6]?.toString(),
                        password = row[7]?.toString() ?: "",
                        isAdmin = false, // 기본값 설정
                        updatedAt = LocalDateTime.now()
                    )
                } catch (e: Exception) {
                    println("데이터 변환 중 오류 발생: ${e.message}")
                    null // 오류가 발생한 행은 무시
                }
            }

            // 저장하기 전에 중복 제거 (중복된 employeeNumber를 기준으로 처리)
            val existingUsers = usersRepository.findAllById(users.map { it.employeeNumber })
            val newUsers = users.filter { user -> existingUsers.none { it.employeeNumber == user.employeeNumber } }

            if (newUsers.isNotEmpty()) {
                usersRepository.saveAll(newUsers)
                println("새로운 사용자 ${newUsers.size}명을 저장했습니다.")
            } else {
                println("저장할 새로운 사용자가 없습니다.")
            }
        } else {
            println("Google Sheets에서 데이터를 찾을 수 없습니다.")
        }
    }
}
