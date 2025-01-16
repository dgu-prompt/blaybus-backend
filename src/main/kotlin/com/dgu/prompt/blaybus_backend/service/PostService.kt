package com.dgu.prompt.blaybus_backend.service

import com.dgu.prompt.blaybus_backend.data.dto.PostResponse
import com.dgu.prompt.blaybus_backend.data.dto.UpdatePostRequest
import com.dgu.prompt.blaybus_backend.data.dto.UpdatedPostResponse
import com.dgu.prompt.blaybus_backend.data.entity.Post
import com.dgu.prompt.blaybus_backend.data.repository.PostRepository
import jakarta.persistence.LockModeType
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class PostService(
    private val postRepository: PostRepository
) {
    fun getPost(postId: Int): PostResponse {
        val post = postRepository.findByPostId(postId)
            ?: throw IllegalArgumentException("Post not found with ID: $postId")

        return PostResponse(
            postId = post.postId,
            title = post.postTitle,
            content = post.content,
            createdAt = post.createdAt,
            updatedAt = post.updatedAt,
            viewCount = post.viewCount
        )
    }

    @Transactional
    fun deletePost(postId: Int) {
        // Post 객체를 조회
        val post = postRepository.findByPostId(postId)
            ?: throw NoSuchElementException("Post not found with ID: $postId")

        try {
            // Post 삭제
            postRepository.delete(post)
        } catch (e: Exception) {
            // 삭제 중 예외 처리
            throw IllegalStateException("Failed to delete Post with ID: $postId. Reason: ${e.message}", e)
        }
    }

    fun updatePost(postId: Int, updateRequest: UpdatePostRequest): UpdatedPostResponse {
        // 게시글을 찾습니다.
        val post = postRepository.findById(postId).orElseThrow {
            NoSuchElementException("Post not found with id: $postId")
        }

        // 게시글 정보를 업데이트하여 새로운 객체를 생성합니다.
        val updatedPost = post.copy(
            postTitle = updateRequest.postTitle ?: post.postTitle,
            content = updateRequest.content ?: post.content,
            updatedAt = LocalDateTime.now(),  // 항상 값을 설정
            viewCount = post.viewCount // viewCount 그대로 유지
        )

        // 업데이트된 게시글을 저장합니다.
        val savedPost = postRepository.save(updatedPost)

        // 업데이트된 게시글을 응답 객체로 반환합니다.
        return UpdatedPostResponse(
            postId = savedPost.postId,
            title = savedPost.postTitle,
            content = savedPost.content,
            createdAt = savedPost.createdAt, // createdAt 추가
            updatedAt = savedPost.updatedAt ?: LocalDateTime.now(), // nullable 처리
            viewCount = savedPost.viewCount
        )
    }

}
