package com.example.data.repository.lesson

import com.example.data.models.Lesson
import com.example.data.requests.LessonRequest
import io.ktor.server.application.*


interface LessonRepository {

    suspend fun createLesson(lesson: Lesson, app: Application): Boolean

    suspend fun updateLesson(
        lessonId: String,
        lessonThumbnailUrl: String?,
        videoUrl: String?,
        updateLessonRequest: LessonRequest,
        app: Application
    ): Boolean

    suspend fun deleteLesson(lessonId: String): Boolean

    suspend fun deleteLessonsFromCourse(courseId: String): Boolean

    suspend fun getLessonsForCourse(courseId: String): List<Lesson>?

    suspend fun getLesson(lessonId: String): Lesson?

}