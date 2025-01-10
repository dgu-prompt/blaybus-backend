package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.Level
import org.springframework.data.jpa.repository.JpaRepository

interface LevelRepository : JpaRepository<Level, String>
