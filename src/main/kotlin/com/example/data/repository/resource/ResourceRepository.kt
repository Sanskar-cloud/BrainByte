package com.example.data.repository.resource
import com.example.data.models.Resource
import com.example.data.requests.ResourceRequest
import com.example.data.requests.ResourceRequestForLesson
import io.ktor.server.application.*


interface ResourceRepository {

    suspend fun getResourcesForCourse(courseId: String, app: Application): List<Resource>?
    suspend fun createResource(resource: ResourceRequest, app: Application): Boolean
    suspend fun createResourceForLesson(resource:ResourceRequestForLesson,app: Application):Boolean
    suspend fun updateResource(resourceId: String, resourceUrl: String?, app: Application): Boolean
    suspend fun deleteResource(resourceId: String,): Boolean
    suspend fun deleteAllResources(courseId: String): Boolean
    suspend fun getResourcesForCourselesson( lessonId: String, app: Application): List<Resource>?
}