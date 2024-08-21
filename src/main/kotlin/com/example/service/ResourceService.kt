package com.example.service

import io.ktor.server.application.*
import com.example.data.models.Resource
import com.example.data.repository.resource.ResourceRepository
import com.example.data.requests.ResourceRequest
import com.example.data.requests.ResourceRequestForLesson

class ResourceService(
    private val resourceRepository: ResourceRepository
) {

    suspend fun getResourcesForCourse(courseId: String, app: Application): List<Resource>? {
        return resourceRepository.getResourcesForCourse(courseId, app)
    }
    suspend fun getResourcesForCourselesson( lessonId: String,app: Application): List<Resource>? {
        return resourceRepository.getResourcesForCourselesson(lessonId,app)
    }

    suspend fun createResourceForLesson(

        lessonId:String,
        resourceUrl: List<String>,
        app: Application
    ): Boolean {
        return resourceRepository.createResourceForLesson(
            ResourceRequestForLesson( lessonId=lessonId,resourceUrl = resourceUrl),
            app = app
        )
    }
    suspend fun createResource(
        courseId: String,
        resourceUrl: List<String>,
        app: Application
    ): Boolean {
        return resourceRepository.createResource(
            ResourceRequest(courseId = courseId, resourceUrl = resourceUrl),
            app = app
        )
    }

    suspend fun updateResource(
        resourceId: String,
        resourceUrl: String?,
        app: Application
    ): Boolean {
        return resourceRepository.updateResource(
            resourceId,
            resourceUrl,
            app
        )
    }
    suspend fun deleteResource(resourceId: String): Boolean {
        return resourceRepository.deleteResource(resourceId)
    }

    suspend fun deleteAllResource(courseId: String): Boolean {
        return resourceRepository.deleteAllResources(courseId)
    }
}