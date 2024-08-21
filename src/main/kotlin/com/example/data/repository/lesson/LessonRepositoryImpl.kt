package com.example.data.repository.lesson

import com.example.data.models.Course
import com.example.data.models.Lesson
import com.example.data.requests.LessonRequest
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList



class LessonRepositoryImpl(
    db: MongoDatabase
): LessonRepository {

    private val courses = db.getCollection<Course>("Course")
    private val lessons = db.getCollection<Lesson>("Lesson")

    override suspend fun createLesson(lesson: Lesson, app: Application): Boolean {
         lessons.insertOne(lesson)
        return courses.updateOne(
            Filters.eq("_id", (lesson.courseId)),


            Updates.inc("noOfLessons", 1)
        ).wasAcknowledged()
    }

    override suspend fun updateLesson(
        lessonId: String,
        lessonThumbnailUrl: String?,
        videoUrl: String?,
        updateLessonRequest: LessonRequest,
        app: Application
    ): Boolean {
        return courses.updateOne(
            Filters.eq("lessonId", lessonId),
            Updates.combine(
                Updates.set("lessonNo", updateLessonRequest.lessonNo),
                Updates.set("name", updateLessonRequest.name),
                Updates.set("thumbnail", lessonThumbnailUrl),
                Updates.set("desc",  updateLessonRequest.desc),
                Updates.set("lessonVideoUrl", videoUrl)
            )
        ).wasAcknowledged()


    }

    override suspend fun deleteLesson(lessonId: String): Boolean {
        return lessons.deleteOne(Filters.eq("_id",lessonId)).wasAcknowledged()

    }

    override suspend fun deleteLessonsFromCourse(courseId: String): Boolean {
        return lessons.deleteMany(Filters.eq("courseId",courseId)).wasAcknowledged()
    }

    override suspend fun getLessonsForCourse(courseId: String): List<Lesson>? {
        return lessons.find(Filters.eq("courseId",courseId)).toList()
    }


    override suspend fun getLesson(lessonId: String): Lesson? {
        return lessons.find(Filters.eq("_id",lessonId)).firstOrNull()
    }
}