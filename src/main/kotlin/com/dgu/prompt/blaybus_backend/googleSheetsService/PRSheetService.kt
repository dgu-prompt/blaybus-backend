package com.dgu.prompt.blaybus_backend.googleSheetsService

import com.dgu.prompt.blaybus_backend.data.entity.Exp2
import com.dgu.prompt.blaybus_backend.data.entity.ExpType
import com.dgu.prompt.blaybus_backend.data.entity.Users2
import com.dgu.prompt.blaybus_backend.data.repository.Exp2Repository
import com.dgu.prompt.blaybus_backend.data.repository.Users2Repository
import com.google.api.services.sheets.v4.Sheets
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PRSheetService(
    private val sheets: Sheets,
    private val exp2Repository: Exp2Repository,
    private val users2Repository: Users2Repository
) {
    private val SPREADSHEET_ID = "1gNAIcvtjcarYJ-L9lbzno3pQqmGjdDoItNw9P324Q7c" // Google Sheets ID
    private val FIRST_RANGE = "참고. 인사평가!B10:E" // HR_FIRST 데이터 범위
    private val SECOND_RANGE = "참고. 인사평가!H10:K" // HR_SECOND 데이터 범위

    fun syncPRExpData() {
        try {
            val firstResponse = sheets.spreadsheets().values().get(SPREADSHEET_ID, FIRST_RANGE).execute()
            val secondResponse = sheets.spreadsheets().values().get(SPREADSHEET_ID, SECOND_RANGE).execute()

            val firstValues = firstResponse.getValues()
            val secondValues = secondResponse.getValues()

            val currentYear = 2024

            // HR_FIRST 데이터 처리
            val firstExpData = firstValues.mapNotNull { row ->
                try {
                    val employeeNumber = row[0].toString().toIntOrNull()
                        ?: throw Exception("유효하지 않은 사용자 번호: ${row[0]}")

                    val expDo = row[3].toString().toInt()
                    val user = users2Repository.findByEmployeeNumber(employeeNumber)
                        ?: throw Exception("사용자 $employeeNumber 를 찾을 수 없습니다.")

                    Exp2(
                        user2 = user,
                        expYear = currentYear,
                        expDo = expDo,
                        expType = ExpType.HR_FIRST,
                        updatedAt = LocalDateTime.now()
                    )
                } catch (e: Exception) {
                    println("HR_FIRST 데이터 변환 중 오류 발생: ${e.message}")
                    null
                }
            }

            // HR_SECOND 데이터 처리
            val secondExpData = secondValues.mapNotNull { row ->
                try {
                    val employeeNumber = row[0].toString().toIntOrNull()
                        ?: throw Exception("유효하지 않은 사용자 번호: ${row[0]}")

                    val expDo = row[3].toString().toInt()
                    val user = users2Repository.findByEmployeeNumber(employeeNumber)
                        ?: throw Exception("사용자 $employeeNumber 를 찾을 수 없습니다.")

                    Exp2(
                        user2 = user,
                        expYear = currentYear,
                        expDo = expDo,
                        expType = ExpType.HR_SECOND,
                        updatedAt = LocalDateTime.now()
                    )
                } catch (e: Exception) {
                    println("HR_SECOND 데이터 변환 중 오류 발생: ${e.message}")
                    null
                }
            }

            // 데이터 저장
            val allExpData = firstExpData + secondExpData
            exp2Repository.saveAll(allExpData)
            println("참고. 인사평가 Sheet 데이터를 동기화했습니다. 총 ${allExpData.size}개 항목이 저장되었습니다.")
        } catch (e: Exception) {
            println("참고. 인사평가 Sheet 데이터 동기화 중 오류 발생: ${e.message}")
        }
    }
}
