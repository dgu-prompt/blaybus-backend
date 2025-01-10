package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.LeaderQuestProgress
import org.springframework.data.jpa.repository.JpaRepository

interface LeaderQuestProgressRepository : JpaRepository<LeaderQuestProgress, Int>
