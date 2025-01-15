package com.dgu.prompt.blaybus_backend.controller

import com.dgu.prompt.blaybus_backend.data.dto.PostResponse
import com.dgu.prompt.blaybus_backend.service.PostService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/post")
class PostController(
    private val postService: PostService
) {
    @GetMapping("/{post_id}")
    fun getPost(@PathVariable("post_id") postId: Int): ResponseEntity<PostResponse> {
        val postResponse = postService.getPost(postId)
        return ResponseEntity.ok(postResponse)
    }
    @DeleteMapping("/{post_id}")
    fun deletePost(@PathVariable("post_id") postId: Int): ResponseEntity<String>{
        postService.deletePost(postId)
        return ResponseEntity.ok("Post deleted successfully")
    }

}
