package com.dgu.prompt.blaybus_backend.googleSheetsService

import com.dgu.prompt.blaybus_backend.data.entity.Level2
import com.dgu.prompt.blaybus_backend.data.repository.Level2Repository
import com.google.api.services.sheets.v4.Sheets
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class LevelSheetService(
    private val sheets: Sheets,
    private val level2Repository: Level2Repository
) {
    private val SPREADSHEET_ID = "1gNAIcvtjcarYJ-L9lbzno3pQqmGjdDoItNw9P324Q7c" // Google Sheets ID
    private val RANGE = "참고. 레벨별 경험치!B8:I" // 데이터 범위 (헤더 제외)

    fun syncLevels() {
        try {
            val response = sheets.spreadsheets().values().get(SPREADSHEET_ID, RANGE).execute()
            val values = response.getValues()

            if (!values.isNullOrEmpty()) {
                val levelGroups = listOf("F", "B", "G") // 그룹 이름 (열 구분용)
                val levels = mutableListOf<Level2>()

                values.forEachIndexed { rowIndex, row ->
                    for (colIndex in 0 until row.size step 3) { // 열 간격 3씩 이동 (F, B, G 그룹)
                        try {
                            val levelName = row.getOrNull(colIndex)?.toString() ?: continue
                            val requiredExp = row.getOrNull(colIndex + 1)?.toString()?.replace(",", "")?.toLongOrNull() ?: continue

                            // 그룹 구하기
                            val groupIndex = colIndex / 3
                            val levelGroup = levelGroups[groupIndex]

                            // levelId 생성
                            val levelId = "$levelGroup-$levelName"

                            // Level 엔티티 생성
                            levels.add(
                                Level2(
                                    levelId = levelId,
                                    requiredExpDo = requiredExp,
                                    updatedAt = LocalDateTime.now()
                                )
                            )
                        } catch (e: Exception) {
                            println("Level 데이터 변환 중 오류 발생 (Row: ${rowIndex + 1}, Col: ${colIndex + 1}): ${e.message}")
                        }
                    }
                }

                // 중복 제거 및 저장
                val existingLevels = level2Repository.findAllById(levels.map { it.levelId })
                val newLevels = levels.filter { level -> existingLevels.none { it.levelId == level.levelId } }

                if (newLevels.isNotEmpty()) {
                    level2Repository.saveAll(newLevels)
                    println("새로운 레벨 ${newLevels.size}개를 저장했습니다.")
                } else {
                    println("저장할 새로운 레벨이 없습니다.")
                }
            } else {
                println("Level Sheet에서 데이터를 찾을 수 없습니다.")
            }
        } catch (e: Exception) {
            println("Level Sheet 데이터 동기화 중 오류 발생: ${e.message}")
        }
    }
}
