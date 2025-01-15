package com.dgu.prompt.blaybus_backend.controller

import com.dgu.prompt.blaybus_backend.service.PostsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/posts")
class PostsController(
    private val postsService: PostsService
) {

    // 게시글 목록 조회
    @GetMapping("/{page}")
    fun getPosts(@PathVariable("page") page: Int): ResponseEntity<*> {
        val response = postsService.getPosts(page)
        return ResponseEntity.ok(response)
    }
}
