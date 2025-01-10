package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.LeaderQuest
import org.springframework.data.jpa.repository.JpaRepository

interface LeaderQuestRepository : JpaRepository<LeaderQuest, Int>
