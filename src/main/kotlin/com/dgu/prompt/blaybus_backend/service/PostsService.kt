package com.dgu.prompt.blaybus_backend.service

import com.dgu.prompt.blaybus_backend.data.dto.PaginationResponse
import com.dgu.prompt.blaybus_backend.data.dto.PostsPageResponse
import com.dgu.prompt.blaybus_backend.data.dto.PostsResponse
import com.dgu.prompt.blaybus_backend.data.repository.PostsRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostsService(
    private val postsRepository: PostsRepository
) {
    @Transactional(readOnly = true)
    fun getPosts(page: Int): PostsPageResponse {
        val postsPerPage = 10

        // 전체 게시글 수 가져오기
        val totalPosts = postsRepository.countTotalPosts().toInt()
        val totalPages = (totalPosts + postsPerPage - 1) / postsPerPage

        // 요청한 페이지 범위 초과 예외 처리
        if (page < 1 || page > totalPages) {
            throw IllegalArgumentException("Invalid page number: $page. Must be between 1 and $totalPages")
        }

        // 페이지 번호에 해당하는 게시글 가져오기
        val allPosts = postsRepository.findAllPostsOrdered()
        val startIdx = (page - 1) * postsPerPage
        val endIdx = minOf(page * postsPerPage, totalPosts)
        val posts = allPosts.subList(startIdx, endIdx)

        // PostsResponse로 변환
        val postResponses = posts.map { post ->
            PostsResponse(
                postId = post.postId,
                title = post.postTitle,
                content = post.content,
                createdAt = post.createdAt,
                updatedAt = post.updatedAt
            )
        }

        return PostsPageResponse(
            posts = postResponses,
            pagination = PaginationResponse(
                currentPage = page,
                totalPages = totalPages,
                totalPosts = totalPosts
            )
        )
    }
}
