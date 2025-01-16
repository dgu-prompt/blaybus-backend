package com.dgu.prompt.blaybus_backend.data.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "post")
data class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    var postId: Int, // var로 변경

    @Column(name = "post_title", nullable = false)
    var postTitle: String, // var로 변경

    @Column(name = "content")
    var content: String, // var로 변경

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime, // var로 변경

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null, // var로 변경

    @Column(name = "view_count", nullable = false)
    var viewCount: Int // var로 변경
)
