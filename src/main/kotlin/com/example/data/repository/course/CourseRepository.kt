package com.example.data.repository.course

import UserTchr
import com.example.data.models.*
import com.example.data.requests.CourseRequest
import com.example.util.Constants
import io.ktor.http.*
import io.ktor.server.application.*


interface CourseRepository {

    suspend fun searchCourses(
        query: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        app: Application
    ): List<Course>?
    suspend fun getCourseById(id: String): Course?

    suspend fun getCoursesForProfile(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        app: Application
    ): List<CourseOverview>?
    suspend fun getMostHighlyRatedCourses(
        tchrId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        app: Application
    ): List<CourseOverview>?
    suspend fun getMostHighlyEnrolledCourses(
        tchrId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        app: Application
    ): List<CourseOverview>?
    suspend fun markCourseAsWatched(studentId: String, courseId: String)



    suspend fun getMostWatchedCourses(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        app: Application
    ): List<CourseOverview>?
    suspend fun getPreviousWatchedCourses(
        studentId:String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE): List<CourseOverview>?
    suspend fun getOthersWatchedCourses(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE): List<CourseOverview>?
    suspend fun getCourseDetails(courseId: String, app: Application): Course?


    suspend fun AddCourseToBookmark(courseId: String, userId: String): Boolean
    suspend fun bookmarkexist(courseId: String, userId: String): Boolean
    suspend fun getBookmarked(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        userId: String
    ): List<CourseOverview?>

    suspend fun getTeacherInfoForCourse(userId: String): UserTchr?
    suspend fun enrollCourse(courseId: String, userId: String, app:Application): Boolean
    suspend fun findStudentByPayment(courseId: String,userId: String,app: Application,status: PaymentStatus=PaymentStatus.COMPLETED):Boolean
    suspend fun hasEnrolledCourse(courseId: String, userId: String): Boolean
    suspend fun rateCourse(courseId: String, userId: String, ratingValue: Float, app: Application): Boolean
    suspend fun getAvgRating(courseId: String, app: Application): Double

    /*Teacher Access*/
    suspend fun createCourse(course: Course): Boolean
    suspend fun getCoursesForTchr(
        tchrId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        app: Application
    ): List<CourseOverview>?

    suspend fun updateCourseInfo(courseId: String, imageUrl:String?, courseRequest: CourseRequest): Boolean?
    suspend fun deleteCourse(courseId: String): Boolean


}