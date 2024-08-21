package com.example.service

import UserTchr
import io.ktor.server.application.*
import com.example.data.models.*
import com.example.data.repository.course.CourseRepository
import com.example.data.requests.CourseRequest
import com.example.data.requests.LessonRequest
import com.example.util.Constants

class CourseService(
    private val courseRepository: CourseRepository
) {


    suspend fun searchCourses(
        query: String,
        page: Int,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        app: Application
    ): List<Course>? {
        return courseRepository.searchCourses(query, page, pageSize, app)
    }
    suspend fun getCourseById(courseId: String): Course? {
        return courseRepository.getCourseById(courseId)
    }


    suspend fun getCoursesForProfile(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        app: Application
    ): List<CourseOverview>? {
        return courseRepository.getCoursesForProfile(userId, page, pageSize, app)
    }
    suspend fun getCoursesForTchr(
        tchrId: String,
        page: Int,
        pageSize: Int,
        app: Application
    ): List<CourseOverview>? {
        return courseRepository.getCoursesForTchr(tchrId, page, pageSize, app)
    }
    suspend fun getMostHighlyRatedCourses(
        tchrId: String,
        page: Int,
        pageSize: Int,
        app: Application
    ): List<CourseOverview>? {
        return courseRepository.getMostHighlyRatedCourses(tchrId, page, pageSize, app)

    }

    suspend fun getMostHighlyEnrolledCourses(
        tchrId: String,
        page: Int,
        pageSize: Int,
        app: Application
    ): List<CourseOverview>? {
        return courseRepository.getMostHighlyEnrolledCourses(tchrId,page,pageSize,app)
    }
    suspend fun findStudentByPayment(
        courseId: String,
        userId: String,
        app: Application,
        status: PaymentStatus
    ):Boolean {
        return courseRepository.findStudentByPayment(courseId,userId,app,status)
    }



        suspend fun getMostWatchedCourses(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        app: Application
    ): List<CourseOverview>? {
        return courseRepository.getMostWatchedCourses(page, pageSize, app)
    }
    suspend fun markCourseAsWatched(studentId: String, courseId: String) {
        return courseRepository.markCourseAsWatched(studentId,courseId)


    }
    suspend fun getPreviousWatchedCourses(
        studentId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE): List<CourseOverview>? {
        return courseRepository.getPreviousWatchedCourses(studentId,page, pageSize)
    }
    suspend fun getOthersWatchedCourses(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE): List<CourseOverview>? {
        return courseRepository.getOthersWatchedCourses(page, pageSize)
    }
    suspend fun getCourseDetails(courseId: String, app: Application): Course? {
        return courseRepository.getCourseDetails(courseId, app)
    }

    private suspend fun getTeacherInfoForCourse(userId: String): UserTchr? {
        return courseRepository.getTeacherInfoForCourse(userId)
    }

    suspend fun createCourse(courseIntroVideoUrl: String, imageUrl: String?, courseRequest: CourseRequest, userId: String): Boolean {
        return courseRepository.createCourse(
            Course(
                courseTitle = courseRequest.courseTitle,
                courseThumbnail = imageUrl?: Constants.DEFAULT_BANNER_IMAGE_PATH,
                description = courseRequest.description,
                price = courseRequest.price,
                courseTeacher = getTeacherInfoForCourse(userId),
                noOfLessons = 0,
                duration = courseRequest.duration,
                tag = courseRequest.tag,
                courseIntroVideoUrl = courseIntroVideoUrl
            )
        )
    }
    suspend fun AddCourseToBookmark(courseId: String, userId: String): Boolean{
        return courseRepository.AddCourseToBookmark(courseId,userId)
    }
    suspend fun getbm(page: Int, pageSize: Int, userId: String): List<CourseOverview?>{
        return courseRepository.getBookmarked(page,pageSize,userId)
    }

    suspend fun bookmarkedexist(courseId: String,userId: String): Boolean {
        return courseRepository.bookmarkexist(courseId, userId)
    }
    suspend fun updateCourseInfo(
        courseId: String,
        imageUrl: String?,
        courseRequest: CourseRequest,
    ): Boolean? {
        return courseRepository.updateCourseInfo(courseId, imageUrl,courseRequest)
    }

    suspend fun deleteCourse(courseId: String): Boolean {
        return courseRepository.deleteCourse(courseId)
    }

    suspend fun enrollCourse(courseId: String, userId: String, app:Application): Boolean {
        return courseRepository.enrollCourse(courseId, userId, app)
    }

    suspend fun hasEnrolledCourse(courseId: String, userId: String): Boolean {
        return courseRepository.hasEnrolledCourse(courseId, userId)
    }

    suspend fun rateCourse(courseId: String, userId: String, ratingValue:   Float, app: Application): Boolean {
        return courseRepository.rateCourse(courseId, userId, ratingValue, app)
    }



}