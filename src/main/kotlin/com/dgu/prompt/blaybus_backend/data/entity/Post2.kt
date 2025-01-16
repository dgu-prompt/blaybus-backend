package com.dgu.prompt.blaybus_backend.data.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "post2")
data class Post2(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    val postId: Int, // val로 변경

    @Column(name = "post_title", nullable = false)
    val postTitle: String, // val로 변경

    @Column(name = "content")
    val content: String, // val로 변경

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime, // val로 변경

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null, // val로 변경

    @Column(name = "view_count", nullable = false)
    val viewCount: Int // val로 변경
)
