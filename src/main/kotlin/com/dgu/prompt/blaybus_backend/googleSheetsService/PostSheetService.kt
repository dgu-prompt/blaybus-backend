package com.dgu.prompt.blaybus_backend.googleSheetsService

import com.dgu.prompt.blaybus_backend.NotificationManager
import com.dgu.prompt.blaybus_backend.data.dto.NotificationRequest
import com.dgu.prompt.blaybus_backend.data.entity.NotificationType
import com.dgu.prompt.blaybus_backend.data.entity.Post
import com.dgu.prompt.blaybus_backend.data.repository.FcmTokenRepository
import com.dgu.prompt.blaybus_backend.data.repository.PostRepository
import com.dgu.prompt.blaybus_backend.data.repository.UsersRepository
import com.dgu.prompt.blaybus_backend.service.NotificationService
import com.google.api.services.sheets.v4.Sheets
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PostSheetService(
    private val sheets: Sheets,
    private val postRepository: PostRepository,
    private val userRepository: UsersRepository,
    private val notificationService: NotificationService,
    private val fcmTokenRepository: FcmTokenRepository,
) {
    private val SPREADSHEET_ID = "1gNAIcvtjcarYJ-L9lbzno3pQqmGjdDoItNw9P324Q7c" // Google Sheets ID
    private val RANGE = "참고. 게시판!B7:D" // 데이터 범위 (헤더 제외)

    @Transactional
    fun syncPosts() {
        try {
            val response = sheets.spreadsheets().values().get(SPREADSHEET_ID, RANGE).execute()
            val values = response.getValues()
            val notificationManager = NotificationManager(userRepository, fcmTokenRepository, notificationService)


            if (!values.isNullOrEmpty()) {
                val postsToSave = values.mapNotNull { row ->
                    try {
                        val postNumber = row[0]?.toString()?.toInt() ?: throw IllegalArgumentException("번호 없음")
                        val title = row[1]?.toString() ?: throw IllegalArgumentException("제목 없음")
                        val content = if (row.size > 2) row[2]?.toString() ?: "" else ""

                        val existingPost = postRepository.findByPostId(postNumber)
                        if (existingPost != null) {
                            if (existingPost.postTitle != title || existingPost.content != content) {
                                existingPost.copy (
                                    postTitle = title,
                                    content = content,
                                    updatedAt = LocalDateTime.now()
                                )
                                postRepository.save(existingPost)
                                existingPost
                            } else null
                        } else {
                            Post(
                                postId = 0,
                                postTitle = title,
                                content = content,
                                createdAt = LocalDateTime.now(),
                                updatedAt = null,
                                viewCount = 0
                            )
                        }
                    } catch (e: Exception) {
                        println("데이터 변환 중 오류 발생: ${e.message}")
                        null
                    }
                }

                if (postsToSave.isNotEmpty()) {
                    postRepository.saveAll(postsToSave)
                    println("게시글 데이터 동기화가 완료되었습니다.")

                    val updatedPostTitles = postsToSave.map { it.postTitle }
                    val user = userRepository.findAUserByEmployeeNumber(2021030101)
                    val fcmToken = user?.let { fcmTokenRepository.findByUser(it)?.fcmToken }
                    if (fcmToken != null) {
                        notificationService.sendNotification(
                            NotificationRequest(
                                employeeNumber = user.employeeNumber,
                                type = NotificationType.SUCCESS,
                                period = " 게시글이 업데이트되었습니다" // ${updatedPostTitles.joinToString(", ")}"
                            )
                        )
                    }
                } else {
                    println("업데이트된 게시글이 없습니다.")
                }
            } else {
                println("게시글 시트에서 데이터를 찾을 수 없습니다.")
            }
        } catch (e: Exception) {
            println("게시글 데이터 동기화 중 오류 발생: ${e.message}")
            e.printStackTrace()
        }
    }
}


//val user = userRepository.findAUserByEmployeeNumber(2021030101)
//val fcmToken = user?.let { fcmTokenRepository.findByUser(it)?.fcmToken }
