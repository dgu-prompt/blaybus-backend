package com.dgu.prompt.blaybus_backend.controller

import com.dgu.prompt.blaybus_backend.data.dto.PostsPageResponse
import com.dgu.prompt.blaybus_backend.service.PostsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/posts")
class PostsController(
    private val postsService: PostsService
) {
    @GetMapping
    fun getPosts(@RequestParam("page") page: Int): ResponseEntity<PostsPageResponse> {
        val postsResponse = postsService.getPosts(page)
        return ResponseEntity.ok(postsResponse)
    }
}
