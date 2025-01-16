package com.dgu.prompt.blaybus_backend.controller

import org.springframework.http.HttpStatus
import com.dgu.prompt.blaybus_backend.data.dto.PostResponse
import com.dgu.prompt.blaybus_backend.data.dto.UpdatePostRequest
import com.dgu.prompt.blaybus_backend.data.dto.UpdatedPostResponse
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
    fun deletePost(@PathVariable("post_id") postId: Int): ResponseEntity<String> {
        return try {
            postService.deletePost(postId)
            ResponseEntity.ok("Post deleted successfully")
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Post with ID $postId not found.")
        } catch (e: IllegalStateException) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: Failed to delete Post. Reason: ${e.message}")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred: ${e.message}")
        }
    }

    @PutMapping("/{post_id}")
    fun updatePost(
        @PathVariable("post_id") postId: Int,
        @RequestBody updateRequest: UpdatePostRequest
    ): ResponseEntity<String> {
        return try {
            val updatedPost = postService.updatePost(postId, updateRequest)
            ResponseEntity.ok("Post updated successfully")
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }
    }


}
