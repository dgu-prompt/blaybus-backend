package com.dgu.prompt.blaybus_backend.service

import com.dgu.prompt.blaybus_backend.data.dto.ProjectResponse
import com.dgu.prompt.blaybus_backend.data.repository.ProjectRepository
import com.dgu.prompt.blaybus_backend.security.JwtUtil
import org.springframework.stereotype.Service

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
    private val jwtUtil: JwtUtil
) {
    fun getProjectsByEmployeeNumber(employeeNumber: Int): List<ProjectResponse> {
        val projects = projectRepository.findByUser2_EmployeeNumber(employeeNumber)
        return projects.map { project ->
            ProjectResponse(
                projectId = project.projectId,
                projectExpDo = project.projectExpDo,
                projectName = project.projectName,
                description = project.description
            )
        }
    }
}