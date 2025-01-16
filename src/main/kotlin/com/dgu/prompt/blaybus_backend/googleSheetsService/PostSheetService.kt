package com.dgu.prompt.blaybus_backend.googleSheetsService

import com.dgu.prompt.blaybus_backend.data.entity.Post
import com.dgu.prompt.blaybus_backend.data.repository.PostRepository
import com.google.api.services.sheets.v4.Sheets
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PostSheetService(
    private val sheets: Sheets,
    private val postRepository: PostRepository
) {
    private val SPREADSHEET_ID = "1gNAIcvtjcarYJ-L9lbzno3pQqmGjdDoItNw9P324Q7c" // Google Sheets ID
    private val RANGE = "참고. 게시판!B7:D" // 데이터 범위 (헤더 제외)

    @Transactional
    fun syncPosts(): Int {
        var updatedCount = 0
        try {
            // 구글 시트 데이터 가져오기

            val response = sheets.spreadsheets().values().get(SPREADSHEET_ID, RANGE).execute()
            val values = response.getValues()

            if (!values.isNullOrEmpty()) {
                val postsFromSheet = values.mapNotNull { row ->
                    try {
                        val realpostId = row[0]?.toString()?.toInt() ?: throw IllegalArgumentException("번호 없음")
                        val title = row[1]?.toString() ?: throw IllegalArgumentException("제목 없음")
                        val content = if (row.size > 2) row[2]?.toString() ?: "" else ""

                        Post(
                            postId = 0, // 새로운 ID로 처리, 실제로 저장될 때 자동 생성됨
                            postTitle = title,
                            content = content,
                            createdAt = LocalDateTime.now(),
                            updatedAt = null, // 초기 생성 시 updatedAt 비워둠
                            viewCount = 0
                        )
                    } catch (e: Exception) {
                        println("데이터 변환 중 오류 발생: ${e.message}")
                        null
                    }
                }

                // 기존 데이터와 비교하여 업데이트할 항목만 처리
                val existingPosts = postRepository.findAll().associateBy { it.postId }

                val postsToUpdate = postsFromSheet.map { newPost ->
                    val existingPost = existingPosts[newPost.postId]

                    if (existingPost != null) {
                        // 기존 데이터가 있다면 덮어쓰기
                        updatedCount++
                        existingPost.copy(
                            postTitle = newPost.postTitle,  // 제목 덮어쓰기
                            content = newPost.content,      // 내용 덮어쓰기
                            updatedAt = LocalDateTime.now() // updatedAt 갱신
                        )
                    } else {
                        // 새로운 데이터인 경우 그대로 저장
                        updatedCount++
                        newPost
                    }
                }

                // 데이터 저장
                if (postsToUpdate.isNotEmpty()) {
                    postRepository.saveAll(postsToUpdate)
                    println("게시글 데이터를 동기화했습니다. 업데이트된 항목 수: ${postsToUpdate.size}개.")
                } else {
                    println("변경된 데이터가 없습니다.")
                }
            } else {
                println("게시글 데이터를 찾을 수 없습니다.")
            }
        } catch (e: Exception) {
            println("게시글 데이터 동기화 중 오류 발생: ${e.message}")
        }

        // 동기화된 항목 수 반환
        return updatedCount
    }
}
