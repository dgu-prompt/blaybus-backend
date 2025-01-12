    package com.dgu.prompt.blaybus_backend.data.entity

    import jakarta.persistence.*
    import java.time.LocalDateTime
    import java.util.Date

    @Entity
    @Table(name = "job_quest_progress")
    data class JobQuestProgress(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "quest_progress_id")
        val questProgressId: Int,

        @ManyToOne
        @JoinColumns(
            JoinColumn(name = "quest_id", referencedColumnName = "quest_id"),
            JoinColumn(name = "job_group_id", referencedColumnName = "job_group_id"),
            JoinColumn(name = "department_id", referencedColumnName = "department_id")
        )
        val jobQuest: JobQuest, // Composite foreign key to `job_quest`

        @ManyToOne
        @JoinColumn(name = "employee_number", referencedColumnName = "employee_number", nullable = false)
        val user: Users, // Foreign key reference to `Users`


        @Enumerated(EnumType.STRING)
        @Column(name = "status", nullable = false)
        val status: ProgressStatus = ProgressStatus.PENDING,

        @Column(name = "updated_at", nullable = false)
        val updatedAt: LocalDateTime,

        @Column(name = "period", nullable = false)
        val period: Int,

        @Enumerated(EnumType.STRING)
        @Column(name = "frequency_type", nullable = false)
        val frequencyType: FrequencyType
    )

    enum class ProgressStatus {
        PENDING, MEDIUM, MAX
    }
