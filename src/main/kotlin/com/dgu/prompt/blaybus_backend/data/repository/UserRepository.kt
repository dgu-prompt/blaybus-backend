package com.dgu.prompt.blaybus_backend.data.repository;

import com.dgu.prompt.blaybus_backend.data.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

interface UserRepository : JpaRepository<User, Int>

