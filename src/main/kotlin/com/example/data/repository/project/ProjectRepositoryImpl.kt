package com.example.data.repository.project

import com.example.data.models.Project
import com.example.data.requests.ProjectRequest
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList



class ProjectRepositoryImpl(
    db: MongoDatabase
): ProjectRepository {

    private val projects = db.getCollection<Project>("Project")
    override suspend fun getProject(projectId: String): Project? {
        return projects.find(Filters.eq("projectId",projectId)).firstOrNull()
    }

    override suspend fun getAllProject(courseId: String): List<Project>? {
        return projects.find(Filters.eq("courseId",courseId)).toList()
    }

    override suspend fun createProject(project: Project): Boolean {
        return projects.insertOne(project).wasAcknowledged()
    }

    override suspend fun updateProject(request: ProjectRequest, projectId: String): Boolean {
        return projects.updateOne(
            Filters.eq("projectId",projectId),
            Updates.combine(
            Updates.set("projectName",request.projectName),
            Updates.set("desc",request.desc),
            Updates.set("imageUrls",request.imageUrls)
            )



        ).wasAcknowledged()
    }

    override suspend fun deleteProject(projectId: String): Boolean {
        return projects.deleteOne(Filters.eq("projectId",projectId)).wasAcknowledged()
    }

    override suspend fun deleteAllProject(courseId: String): Boolean {
        return projects.deleteMany(Filters.eq("courseId",courseId)).wasAcknowledged()
    }
}