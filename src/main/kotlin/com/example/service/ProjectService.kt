package com.example.service

import com.example.data.models.Project
import com.example.data.repository.project.ProjectRepository
import com.example.data.requests.ProjectRequest

class ProjectService(
    private val projectRepository: ProjectRepository
) {

    suspend fun getProject(projectId: String): Project? {
        return projectRepository.getProject(projectId)
    }
    suspend fun getAllProject(courseId: String): List<Project>? {
        return projectRepository.getAllProject(courseId)
    }

    suspend fun createProject(courseId: String, request: ProjectRequest): Boolean {
        return projectRepository.createProject(
            Project(
                courseId = courseId,
                projectName = request.projectName,
                desc = request.desc,
                imageUrls = request.imageUrls
            )
        )
    }

    suspend fun updateProject(request: ProjectRequest, projectId: String): Boolean {
        return projectRepository.updateProject(request, projectId)
    }
    suspend fun deleteProject(projectId: String): Boolean {
        return projectRepository.deleteProject(projectId)
    }
    suspend fun deleteAllProject(courseId: String): Boolean {
        return projectRepository.deleteAllProject(courseId)
    }

}