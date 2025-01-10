package com.dgu.prompt.blaybus_backend.data.repository

import com.dgu.prompt.blaybus_backend.data.entity.JobGroup
import org.springframework.data.jpa.repository.JpaRepository

interface JobGroupRepository : JpaRepository<JobGroup, Int>
