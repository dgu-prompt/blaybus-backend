package com.dgu.prompt.blaybus_backend.data.repository
import com.dgu.prompt.blaybus_backend.data.entity.Exp
import org.springframework.data.jpa.repository.JpaRepository

interface ExpRepository : JpaRepository<Exp, Int>
