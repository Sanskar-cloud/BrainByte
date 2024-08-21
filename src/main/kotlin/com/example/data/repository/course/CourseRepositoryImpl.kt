package com.example.data.repository.course

import UserTchr
import com.example.data.models.*
import com.example.data.requests.CourseRequest
import com.mongodb.client.model.Accumulators.avg
import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.match
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import com.mongodb.client.model.Updates.*
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.Document
import org.bson.conversions.Bson

import java.util.*

class CourseRepositoryImpl(db: MongoDatabase): CourseRepository {

    private val courses = db.getCollection<Course>("Course")
    private val users = db.getCollection<User>("User")
    private val enrollments = db.getCollection<Enrollment>("Enrollment")
    private val ratings = db.getCollection<Rating>("Rating")
    private val categories = db.getCollection<Category>("Category")
    private val bookmarks = db.getCollection<Bookmark>("Bookmark")
    private val studentPayment=db.getCollection<StudentPayment>("StudentPayment")
    override suspend fun searchCourses(
        query: String,
        page: Int,
        pageSize: Int,
        app: Application
    ): List<Course>? {
        val filters: Bson = or(
            regex("courseTitle", query, "i"),
            regex("description", query, "i"),
            regex("tag", query, "i"),
                    regex("courseTeacher.username", query, "i")
        )

        return courses.find(filters)
            .skip(page * pageSize)
            .limit(pageSize)
            .toList()
    }


    override suspend fun getCourseById(id: String): Course? {
        return courses.find(Filters.eq("_id", id)).firstOrNull()

    }

    override suspend fun getCoursesForProfile(userId: String, page: Int, pageSize: Int, app: Application): List<CourseOverview>? {
        val enrollments = enrollments.find(Filters.eq("userId", userId)).toList()
        val courseIds = enrollments.map { it.courseId }
        app.log.info("ye jh idddddddddddddddddddddddddddddddddddd"+courseIds.toString())
        val courses = courses.find(Filters.`in`("_id", courseIds))
            .skip(page * pageSize)
            .limit(pageSize)
            .toList()

        // Map to CourseOverview
        val courseOverviews = courses.map { course ->
            CourseOverview(
                courseId = course.courseId,
                courseName = course.courseTitle,
                courseTeacherName = course.courseTeacher?.username!!,
                price = course.price,
                courseThumbnailUrl = course.courseThumbnail,
                rating = course.avgRating,
                noOfStudentRated = course.noOfStudentRated,
                noOfStudentEnrolled = course.noOfStudentEnrolled,
                tag = course.tag,
//                courseIntroVideoUrl = course.courseIntroVideoUrl
            )
        }

        return courseOverviews
    }

    override suspend fun getMostHighlyRatedCourses(tchrId: String,page: Int, pageSize: Int, app: Application): List<CourseOverview>? {
        return  courses.find(Filters.eq("courseTeacher.Teacherid", tchrId))
            .sort(Sorts.descending("avgRating"))
            .skip(page * pageSize)
            .limit(pageSize)
            .toList()
            .map {course ->
                CourseOverview(
                    courseId = course.courseId,
                    courseName = course.courseTitle,
                    courseTeacherName= course.courseTeacher!!.username,
                    courseThumbnailUrl = course.courseThumbnail,
                    price = course.price,
                    rating = course.avgRating,
                    noOfStudentRated = course.noOfStudentRated,
                    noOfStudentEnrolled = course.noOfStudentEnrolled,
                    tag = course.tag,
//                    courseIntroVideoUrl = course.courseIntroVideoUrl

                )
            }



    }

    override suspend fun getMostHighlyEnrolledCourses(
        tchrId: String,
        page: Int,
        pageSize: Int,
        app: Application
    ): List<CourseOverview>? {
        return  courses.find(Filters.eq("courseTeacher.Teacherid", tchrId))
            .sort(Sorts.descending("noOfStudentEnrolled"))
            .skip(page * pageSize)
            .limit(pageSize)
            .toList()
            .map {course ->
                CourseOverview(
                    courseId = course.courseId,
                    courseName = course.courseTitle,
                    courseTeacherName= course.courseTeacher!!.username,
                    courseThumbnailUrl = course.courseThumbnail,
                    price = course.price,
                    rating = course.avgRating,
                    noOfStudentRated = course.noOfStudentRated,
                    noOfStudentEnrolled = course.noOfStudentEnrolled,
                    tag = course.tag,
//                    courseIntroVideoUrl = course.courseIntroVideoUrl

                )
            }

    }

    override suspend fun markCourseAsWatched(studentId: String, courseId: String) {
        val stdnt=users.find(Filters.eq("_id",studentId)).firstOrNull()


        val course=courses.find(Filters.eq("_id",courseId)).firstOrNull()
        val courseOverview = course?.let {
            CourseOverview(
                courseId = it.courseId,
                courseName = course.courseTitle,
                courseTeacherName = course.courseTeacher?.username ?: "",
                courseThumbnailUrl = course.courseThumbnail,
                price = course.price,
                rating = course.avgRating,
                noOfStudentRated = course.noOfStudentRated,
                noOfStudentEnrolled = course.noOfStudentEnrolled,
                tag = course.tag,
//                courseIntroVideoUrl = course.courseIntroVideoUrl
            )
        }




            if (stdnt != null) {
                // Add the course to the watched courses list if it's not already there




                        // Add the course to the watched courses list if it's not already there
                if (courseOverview != null) {
                    if (!stdnt.watchedCourses.any { it.courseId == courseOverview.courseId }) {
                        stdnt.watchedCourses.add(courseOverview)
                        users.updateOne(
                            Filters.eq("_id", studentId),
                            Updates.set("watchedCourses", stdnt.watchedCourses)
                        )
                    }
                }
                    }
                }





    override suspend fun getMostWatchedCourses(page: Int, pageSize: Int, app: Application): List<CourseOverview> {

        val courseOverviews =  courses.find()
            .sort(Sorts.descending("noOfStudentEnrolled"))
            .skip(page * pageSize)
            .limit(pageSize)
            .toList()
            .map { course ->
                app.log.info(course.courseTitle)
                CourseOverview(
                    courseId = course.courseId,
                    courseName = course.courseTitle,
                    courseTeacherName= course.courseTeacher!!.username,
                    price = course.price,
                    courseThumbnailUrl = course.courseThumbnail,
                    rating = course.avgRating,
                    noOfStudentRated = course.noOfStudentRated,
                    noOfStudentEnrolled = course.noOfStudentEnrolled,
                    tag = course.tag,
//                    courseIntroVideoUrl = course.courseIntroVideoUrl
                )

            }

        return courseOverviews

    }

    override suspend fun getPreviousWatchedCourses(studentId:String,page: Int, pageSize: Int): List<CourseOverview>? {
        val stdnt=users.find(Filters.eq("_id",studentId)).firstOrNull()

            return stdnt?.watchedCourses
                ?.drop(page * pageSize)
                ?.take(pageSize)




    }

    override suspend fun getOthersWatchedCourses(page: Int, pageSize: Int): List<CourseOverview>? {
        return courses.find()
            .sort(Sorts.descending("avgRating"))
            .skip(page * pageSize)
            .limit(pageSize)
            .toList()
            .map {course ->
                CourseOverview(
                    courseId = course.courseId,
                    courseName = course.courseTitle,
                    courseTeacherName= course.courseTeacher!!.username,
                    courseThumbnailUrl = course.courseThumbnail,
                    price = course.price,
                    rating = course.avgRating,
                    noOfStudentRated = course.noOfStudentRated,
                    noOfStudentEnrolled = course.noOfStudentEnrolled,
                    tag = course.tag,
//                    courseIntroVideoUrl = course.courseIntroVideoUrl
                )
            }
    }

    override suspend fun getCourseDetails(courseId: String, app: Application): Course? {
        app.log.info(courseId)
        val course = courses.find(Filters.eq("_id", courseId)).firstOrNull()
        app.log.info(course?.courseTitle)
        return course
    }

    override suspend fun AddCourseToBookmark(courseId: String, userId: String): Boolean {
        val user=users.find(Filters.eq("_id",userId))
        val course=courses.find(Filters.eq("_id",courseId)).firstOrNull()
        val courseOverview = course?.let {
            CourseOverview(
                courseId = it.courseId,
                courseName = course.courseTitle,
                courseTeacherName = course.courseTeacher?.username ?: "",
                courseThumbnailUrl = course.courseThumbnail,
                price = course.price,
                rating = course.avgRating,
                noOfStudentRated = course.noOfStudentRated,
                noOfStudentEnrolled = course.noOfStudentEnrolled,
                tag = course.tag,
//                courseIntroVideoUrl = course.courseIntroVideoUrl
            )
        }

//            .map { course->
//                CourseOverview(
//                    courseId = course.courseId,
//                    courseName = course.courseTitle,
//                    courseTeacherName= course.courseTeacher!!.username,
//                    courseThumbnailUrl = course.courseThumbnail,
//                    rating = course.avgRating,
//                    noOfStudentRated = course.noOfStudentRated,
//                    noOfStudentEnrolled = course.noOfStudentEnrolled,
//                    tag = course.tag
//                )
//
//
//            }

        return bookmarks.insertOne(
            Bookmark(
                userId = userId,
                courseId = courseId,
                course = courseOverview






            )
        ).wasAcknowledged()
    }

    override suspend fun bookmarkexist(courseId: String, userId: String): Boolean {
        val isexisting = bookmarks.find(
             Filters.and(
                Filters. eq("courseId", courseId),
                 Filters.eq("userId", userId)

             )
        ).firstOrNull()

        if(isexisting==null){
            return false
        }
        else{
            return true
        }

    }

    override suspend fun getBookmarked(page: Int, pageSize: Int, userId: String): List<CourseOverview?> {
        val courses=bookmarks.find(Filters.eq("userId",userId))
            .skip(page*pageSize)
            .limit(pageSize)
            .toList()
            .map { course->
                course.course?.let {
                    CourseOverview(
                        courseId = it.courseId,
                        courseName = course.course.courseName,
                        courseTeacherName= course.course.courseTeacherName,
                        courseThumbnailUrl = course.course.courseThumbnailUrl,
                        rating = course.course.rating,
                        noOfStudentRated = course.course.noOfStudentRated,

                        noOfStudentEnrolled = course.course.noOfStudentEnrolled,
                        tag = course.course.tag,
//                        courseIntroVideoUrl = course.course.courseIntroVideoUrl
                    )
                }
            }
        return courses
    }

    override suspend fun createCourse(course: Course): Boolean {
        return courses.insertOne(course).wasAcknowledged()
    }

    override suspend fun getCoursesForTchr(
        tchrId: String,
        page: Int,
        pageSize: Int,
        app: Application
    ): List<CourseOverview>? {


            val courseOverviews =  courses.find(Filters.eq("courseTeacher.Teacherid", tchrId))
                .skip(page * pageSize)
                .limit(pageSize)
                .toList()
                .map { course ->
                    app.log.info(course.courseTitle)
                    CourseOverview(
                        courseId = course.courseId,
                        courseName = course.courseTitle,
                        courseTeacherName= course.courseTeacher!!.username,
                        courseThumbnailUrl = course.courseThumbnail,
                        price = course.price,
                        rating = course.avgRating,
                        noOfStudentRated = course.noOfStudentRated,
                        noOfStudentEnrolled = course.noOfStudentEnrolled,
                        tag = course.tag,
//                        courseIntroVideoUrl = course.courseIntroVideoUrl
                    )

                }
            return courseOverviews
        }


    override suspend fun updateCourseInfo(courseId: String, imageUrl:String?, courseRequest: CourseRequest): Boolean? {
        val course = courses.find(Filters.eq("_id", courseId)).firstOrNull()
        if (course != null) {
            return courses.updateOne(
                Filters.eq("courseId", courseId),
                Updates.combine(
                    Updates.set("courseTitle",  courseRequest.courseTitle),
                    Updates.set("description", courseRequest.description),
                    Updates.set("courseThumbnail", imageUrl?: course.courseThumbnail),
                    Updates.set("tag",  courseRequest.tag),
                    Updates.set("duration", courseRequest.duration)
                )

            ).wasAcknowledged()
        }
        else{
            return null
        }
    }

    override suspend fun deleteCourse(courseId: String): Boolean {

        return courses.deleteOne(
            eq("_id", courseId)
        ).wasAcknowledged()
    }
    override suspend fun getTeacherInfoForCourse(userId: String): UserTchr? {
        val user = users.find(Filters.eq("_id", userId)).firstOrNull()
        if (user != null) {
            return UserTchr(
                username = user.username,
                profileImageUrl = user.profileImageUrl,
                Teacherid = userId
            )
        }
        else{
            return null
        }
    }


    override suspend fun enrollCourse(courseId: String, userId: String, app: Application): Boolean {
        val isEnrollmentNotExists = enrollments.find(
            and(
                eq("courseId", courseId),
                eq("userId", userId)
            )
        ).firstOrNull()
        app.log.info("gbkugfiycfycfucjshtrdgdtrhdgndvyjd"+isEnrollmentNotExists.toString())
        if (isEnrollmentNotExists!=null){
            return false
        }
        else{
            enrollments.insertOne(
                Enrollment(userId,courseId,Date())
            )
             val a=users.updateOne(eq("_id",userId),
                inc("courseCount",1)
            ).wasAcknowledged()
            val b=courses.updateOne(
                eq("_id", courseId),
                inc("noOfStudentEnrolled", 1)
            ).wasAcknowledged()

            return a&&b
        }



    }

    override suspend fun findStudentByPayment(
        courseId: String,
        userId: String,
        app: Application,
        status: PaymentStatus
    ):Boolean {
        val res=studentPayment.find(
            Filters.and(
                eq("courseId",courseId),
                eq("StudentId",userId),
                eq("status",status)
            )

        ).firstOrNull()
        if (res==null){
            return false
        }
        else{
            return true
        }
    }

    override suspend fun hasEnrolledCourse(courseId: String, userId: String): Boolean {
        val resultt=enrollments.find(
            Filters.and(
                eq("courseId", courseId),
                eq("userId", userId)
            )
        ).firstOrNull()



        if(resultt==null){
            return false
        }
        else{
            return  true
        }
    }

    override suspend fun rateCourse(courseId: String, userId: String, ratingValue: Float, app: Application): Boolean {
        val isRatingNotExists = ratings.find(
            Filters.and(
                eq("_id", courseId),
                eq("_id", userId)
            )
        ).firstOrNull()

//        if (isRatingNotExists==null){
//            return
//        }


        val isRatedSuccessful = ratings.insertOne(
            Rating(userId,courseId,ratingValue, dateRating = Date())


        ).wasAcknowledged()

        val avgRating = getAvgRating(courseId, app)

        app.log.info("$avgRating")

        return if (isRatedSuccessful) {
            courses.updateOne(
                eq("_id", courseId),
                combine(
                    set("avgRating", avgRating),
                    inc("noOfStudentRated", 1)
                )
            ).wasAcknowledged()
        } else {
            false
        }
    }

    override suspend fun getAvgRating(courseId: String, app: Application): Double {
        val pipeline = listOf(
            match(eq("courseId", courseId)),
            group(eq("courseId", courseId), avg("avg_rating", "\$ratingValue"))
        )

        val result = ratings.aggregate<Document>(pipeline)

        app.log.info("${result.firstOrNull()?.get("avg_rating")}")

        return result.firstOrNull()?.getDouble("avg_rating") ?: 0.0
    }



}