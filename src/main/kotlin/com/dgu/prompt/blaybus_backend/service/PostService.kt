package com.dgu.prompt.blaybus_backend.service

import com.dgu.prompt.blaybus_backend.data.dto.PostResponse
import com.dgu.prompt.blaybus_backend.data.repository.PostRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

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
    fun deletePost(postId: Int){
        postRepository.deleteByPostId(postId)
    }
}
