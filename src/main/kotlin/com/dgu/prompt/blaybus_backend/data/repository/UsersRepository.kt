package com.dgu.prompt.blaybus_backend.data.repository;

import com.dgu.prompt.blaybus_backend.data.entity.Users
import org.springframework.data.jpa.repository.JpaRepository

interface UsersRepository : JpaRepository<Users, Int>

