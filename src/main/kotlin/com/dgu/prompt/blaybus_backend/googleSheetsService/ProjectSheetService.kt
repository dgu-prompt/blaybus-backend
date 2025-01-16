package com.dgu.prompt.blaybus_backend.googleSheetsService

import com.dgu.prompt.blaybus_backend.data.entity.Project
import com.dgu.prompt.blaybus_backend.data.repository.ProjectRepository
import com.dgu.prompt.blaybus_backend.data.repository.UsersRepository
import com.google.api.services.sheets.v4.Sheets
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class ProjectSheetService(
    private val sheets: Sheets,
    private val projectRepository: ProjectRepository,
    private val usersRepository: UsersRepository
) {
    private val SPREADSHEET_ID = "1gNAIcvtjcarYJ-L9lbzno3pQqmGjdDoItNw9P324Q7c" // Google Sheets ID
    private val RANGE = "참고. 전사 프로젝트!B8:H" // 데이터 범위

    @Transactional

    fun syncProjectData() {
        try {
            // 구글 시트 데이터 가져오기
            val response = sheets.spreadsheets().values().get(SPREADSHEET_ID, RANGE).execute()
            val values = response.getValues()

            if (!values.isNullOrEmpty()) {
                val usersMap = usersRepository.findAll().associateBy { it.employeeNumber }

                val projectDataFromSheet = values.mapNotNull { row ->
                    try {
                        val month = row[0]?.toString()?.toIntOrNull() ?: return@mapNotNull null
                        val day = row[1]?.toString()?.toIntOrNull() ?: return@mapNotNull null
                        val employeeNumber = row[2]?.toString()?.toIntOrNull() ?: return@mapNotNull null
                        val projectName = row[4]?.toString() ?: return@mapNotNull null
                        val projectExpDo = row[5]?.toString()?.toIntOrNull() ?: 0
                        val description = row[6]?.toString() ?: ""

                        val projectDate = LocalDate.of(2024, month, day)
                        val user = usersMap[employeeNumber]

                        if (user == null) {
                            println("경고: employeeNumber $employeeNumber 에 해당하는 사용자를 찾을 수 없어 데이터를 무시합니다.")
                            return@mapNotNull null
                        }

                        Triple(
                            employeeNumber,
                            projectDate,
                            Project(
                                projectId = 0, // Auto-generated
                                projectName = projectName,
                                description = description,
                                projectExpDo = projectExpDo,
                                updatedAt = LocalDateTime.now(),
                                projectDate = projectDate,
                                user = user // user 값을 설정
                            )
                        )
                    } catch (e: Exception) {
                        println("프로젝트 데이터 변환 중 오류 발생: ${e.message}")
                        null
                    }
                }

                // 기존 데이터와 비교하여 업데이트할 항목 처리
                val existingProjectData = projectRepository.findAll()
                    .groupBy { it.user.employeeNumber to it.projectName to it.projectDate }

                val projectsToUpdateOrCreate = projectDataFromSheet.mapNotNull { (employeeNumber, projectDate, newProject) ->
                    val existingProject = existingProjectData[employeeNumber to newProject.projectName to projectDate]?.firstOrNull()

                    if (existingProject != null) {
                        // 기존 데이터가 있다면 비교 후 업데이트 결정
                        if (existingProject.projectExpDo != newProject.projectExpDo || existingProject.description != newProject.description) {
                            existingProject.copy(
                                projectExpDo = newProject.projectExpDo,
                                description = newProject.description,
                                updatedAt = LocalDateTime.now()
                            )
                        } else {
                            null // 변경 사항 없으면 무시
                        }
                    } else {
                        // 새로운 데이터라면 그대로 추가
                        newProject
                    }
                }

                if (projectsToUpdateOrCreate.isNotEmpty()) {
                    projectRepository.saveAll(projectsToUpdateOrCreate)
                    println("프로젝트 데이터를 동기화했습니다. 업데이트된 항목 수: ${projectsToUpdateOrCreate.size}개.")
                } else {
                    println("변경된 프로젝트 데이터가 없습니다.")
                }
            } else {
                println("프로젝트 시트에서 데이터를 찾을 수 없습니다.")
            }
        } catch (e: Exception) {
            println("프로젝트 데이터 동기화 중 오류 발생: ${e.message}")
        }
    }
}
