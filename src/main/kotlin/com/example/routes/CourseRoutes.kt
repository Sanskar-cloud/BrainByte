package com.example.routes

import com.example.data.models.PaymentStatus
import com.example.data.repository.aws.FileStorage
import com.example.data.requests.CourseRequest
import com.example.data.requests.RatingRequest
import com.example.data.responses.BasicApiResponse
import com.example.service.CourseService
import com.example.util.QueryParams
import com.example.util.toFile
import io.ktor.client.engine.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Route.addtobookmark(
    courseService: CourseService,
    app: Application
){
    authenticate("auth-eduCo"){
        post("/api/user/bookmark/create") {
            val query = call.parameters[QueryParams.PARAM_COURSE_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val query2 = call.parameters[QueryParams.PARAM_USER_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val previousbookmarked=courseService.bookmarkedexist(query,query2)
            if (previousbookmarked) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = "this course has already been bookmarked by you"
                    )
                )
                return@post
            }
            val bookmarked =courseService.AddCourseToBookmark(query,query2)
            if (bookmarked) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = true,
                        message = "the course has been bookmarked"
                    )
                )

            }

        }

    }
}
fun Route.getbm(
    courseService: CourseService,
    app: Application
){
    authenticate("auth-eduCo"){
        get("/api/user/bookmark/courses") {

            val query2 = call.parameters[QueryParams.PARAM_USER_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toInt()?: kotlin.run {
                app.log.info("yha se toh ni")
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toInt()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val courses = courseService.getbm(
                userId =query2,
                page = page,
                pageSize = pageSize,

            )

            courses?.let {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = BasicApiResponse(
                        successful = true,
                        data = courses
                    )
                )
            }
        }
}}

fun Route.findStdntPay(
    courseService: CourseService,
    app: Application
) {
    get("api/stdnt/pay") {
        val query = call.parameters[QueryParams.PARAM_USER_ID]?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val courseId = call.parameters[QueryParams.PARAM_COURSE_ID]?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
//        val status2=call.parameters["status"]?: kotlin.run {
//            call.respond(HttpStatusCode.BadRequest)
//            return@get
//        }
//        val status = try {
//            PaymentStatus.valueOf(status2)
//        } catch (e: IllegalArgumentException) {
//            call.respond(HttpStatusCode.BadRequest, "Invalid status value")
//            return@get
//        }
        val isOwned = courseService.findStudentByPayment(courseId, query, app, status = PaymentStatus.COMPLETED)
        app.log.info(isOwned.toString())
        if (isOwned) {
            call.respond(
                isOwned,


            )

        }
        else {
            call.respond(
                isOwned,


                )


        }


    }

}


fun Route.searchCourses(
    courseService: CourseService,
    app: Application
) {
    authenticate("auth-eduCo") {
        get("api/user/search/courses") {
            val query = call.parameters[QueryParams.PARAM_QUERY]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toInt()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val courses = courseService.searchCourses(
                page = page,
                query = query,
                app = app
            )

            courses?.let {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = BasicApiResponse(
                        successful = true,
                        data = courses
                    )
                )
            }?: kotlin.run {
                call.respond(
                    HttpStatusCode.OK,
                    message = "No Such Courses are available"
                )
            }
        }
    }
}
fun Route.getCoursesForTchr(
    courseService: CourseService,
    app: Application
) {

    authenticate("auth-eduCo") {
        get("api/tchr/courses") {
            val tchrId = call.parameters[QueryParams.PARAM_TCHR_ID]?: kotlin.run {
                app.log.info("tchr4000")
                println("tchrid 4000")
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toInt()?: kotlin.run {
                app.log.info("yha se toh ni")
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toInt()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val courses = courseService.getCoursesForTchr(
                tchrId = tchrId,
                page = page,
                pageSize = pageSize,
                app = app
            )

            courses?.let {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = BasicApiResponse(
                        successful = true,
                        data = courses
                    )
                )
            }?: kotlin.run {
                call.respond(
                    HttpStatusCode.OK,
                    message = "No Such Courses are available"
                )
            }
        }
    }

}
fun Route.getMostHighlyRatedCourses(
    courseService: CourseService,
    app: Application
) {

    authenticate("auth-eduCo") {
        get("api/tchr/highly/rated/courses") {
            val tchrId = call.parameters[QueryParams.PARAM_TCHR_ID]?: kotlin.run {
                app.log.info("tchr4000")
                println("tchrid 4000")
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toInt()?: kotlin.run {
                app.log.info("yha se toh ni")
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toInt()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val courses = courseService.getMostHighlyRatedCourses(
                tchrId = tchrId,
                page = page,
                pageSize = pageSize,
                app = app
            )

            courses?.let {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = BasicApiResponse(
                        successful = true,
                        data = courses
                    )
                )
            }?: kotlin.run {
                call.respond(
                    HttpStatusCode.OK,
                    message = "No Such Courses are available"
                )
            }
        }
    }

}
fun Route.getMostHighlyEnrolledCourses(
    courseService: CourseService,
    app: Application
) {

    authenticate("auth-eduCo") {
        get("api/tchr/highly/enrolled/courses") {
            val tchrId = call.parameters[QueryParams.PARAM_TCHR_ID]?: kotlin.run {
                app.log.info("tchr4000")
                println("tchrid 4000")
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toInt()?: kotlin.run {
                app.log.info("yha se toh ni")
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toInt()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val courses = courseService.getMostHighlyEnrolledCourses(
                tchrId = tchrId,
                page = page,
                pageSize = pageSize,
                app = app
            )

            courses?.let {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = BasicApiResponse(
                        successful = true,
                        data = courses
                    )
                )
            }?: kotlin.run {
                call.respond(
                    HttpStatusCode.OK,
                    message = "No Such Courses are available"
                )
            }
        }
    }

}
fun Route.getCoursesForProfile(
    courseService: CourseService,
    app: Application
) {

    authenticate("auth-eduCo") {
        get("api/user/profile/courses") {
            val userId = call.parameters[QueryParams.PARAM_USER_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toInt()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toInt()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val courses = courseService.getCoursesForProfile(
                userId = userId,
                page = page,
                pageSize = pageSize,
                app = app
            )

            courses?.let {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = BasicApiResponse(
                        successful = true,
                        data = courses
                    )
                )
            }?: kotlin.run {
                call.respond(
                    HttpStatusCode.OK,
                    message = "No Such Courses are available"
                )
            }
        }
    }

}
fun Route.getCourse(
    courseService: CourseService
) {
    authenticate("auth-eduCo") {
        get("api/user/course") {
            val courseId = call.parameters[QueryParams.PARAM_COURSE_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }


            val course = courseService.getCourseById(courseId)
            course?.let {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = it
                )
            }?: kotlin.run {
                call.respond(
                    HttpStatusCode.OK,
                    message = "No Such Course is available"
                )
            }
        }
    }

}

fun Route.getMostWatchedCourses(courseService: CourseService, app: Application) {
    authenticate("auth-eduCo") {
        get("/api/course/most_watched_courses") {
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toInt()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toInt()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val mostWatchedCourses = courseService.getMostWatchedCourses(page, pageSize, app)
            mostWatchedCourses?.let {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = it
                )
            }?: call.respond(
                HttpStatusCode.NotFound,
                message = "No Courses is available"
            )
        }
    }
}
fun Route.addWtchCrs(
    courseService: CourseService,
    app: Application
){
    authenticate("auth-eduCo"){
        post("/api/user/watch/create") {
            val query = call.parameters[QueryParams.PARAM_COURSE_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val query2 = call.parameters[QueryParams.PARAM_USER_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val bookmarked =courseService.markCourseAsWatched(query,query2)
            if (bookmarked!=    null) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = true,

                    )
                )

            }

        }

    }
}


fun Route.getPreviousWatchedCourses(courseService: CourseService) {
    authenticate("auth-eduCo") {
        get("/api/course/previous_watched_courses") {
//            val query = call.parameters[QueryParams.PARAM_USER_ID]?: kotlin.run {
//                call.respond(HttpStatusCode.BadRequest)
//                return@get
//            }

            val page = call.parameters[QueryParams.PARAM_PAGE]?.toInt()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toInt()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val previousWatchedCourses = courseService.getPreviousWatchedCourses(call.userId,page, pageSize)
            previousWatchedCourses?.let {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = it
                )
            }?: call.respond(
                HttpStatusCode.OK,
                message = "No Courses is available"
            )
        }
    }
}

fun Route.getOthersWatchedCourses(courseService: CourseService) {
    authenticate("auth-eduCo") {
        get("/api/course/others_watched_courses") {
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toInt()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toInt()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val othersWatchedCourses = courseService.getOthersWatchedCourses(page, pageSize)
            othersWatchedCourses?.let {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = it
                )
            }?: kotlin.run {
                call.respond(
                    HttpStatusCode.OK,
                    message = "No Courses is available"
                )
            }
        }
    }
}


fun Route.getCourseDetails(courseService: CourseService, app: Application) {
    get("/api/course/details") {
        val courseId = call.parameters[QueryParams.PARAM_COURSE_ID]?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }

        val courseDetails = courseService.getCourseDetails(courseId, app)
        courseDetails?.let {
            call.respond(
                status = HttpStatusCode.OK,
                message = BasicApiResponse(
                    successful = true,
                    data = courseDetails
                )
            )
        }?: kotlin.run {
            call.respond(
                HttpStatusCode.NotFound,
                message = "Course Details is not available"
            )
            return@get
        }
    }
}






fun Route.createCourse(courseService: CourseService, fileStorage: FileStorage, app: Application) {
    authenticate("auth-eduCo") {
        post("/api/user/tutor/create_course") {
//            val userId = call.parameters[QueryParams.PARAM_USER_ID]?: kotlin.run {
//                call.respond(HttpStatusCode.BadRequest)
//                return@post
//            }
//            app.log.info(userId)
            val multipart = call.receiveMultipart()
            var createCourseRequest: CourseRequest? = null
            var imageurl: String? = null
            var courseIntroVideoUrl: String? = null

            multipart.forEachPart {partData ->
                when(partData) {
                    is PartData.FormItem -> {
                        if (partData.name == "course_data") {
                            createCourseRequest = Json.decodeFromString<CourseRequest>(partData.value)
                        }
                    }
                    is PartData.FileItem -> {

                        if (partData.name == "course_thumbnail") {
                            imageurl = fileStorage.saveToAWSBucket(
                                file = partData.toFile(),
                                bucketName = "coursedata112",
                            )
                            app.log.info("profile Picture Successfully Saved $imageurl")
                        } else if (partData.name == "course_intro_video"){
                            courseIntroVideoUrl = fileStorage.saveToAWSBucket(
                                file = partData.toFile(),
                                bucketName = "coursedata112",
                            )
                            app.log.info("Course Intro Video Successfully Saved $courseIntroVideoUrl")
                        }



                    }

                    else -> Unit
                }
                partData.dispose

            }

            createCourseRequest?.let { request ->
                val updateAcknowledged = courseService.createCourse(
                    imageUrl = if (imageurl == null) {
                        null
                    } else {
                        imageurl
                    },
                    courseRequest = request,
                    userId = call.userId,
                    courseIntroVideoUrl = courseIntroVideoUrl?: ""
                )
                if (updateAcknowledged) {

                    app.log.info("course Successfully Created : $updateAcknowledged")
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true,
                        )
                    )
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
        }
    }
}

fun Route.updateCourseInfo(courseService: CourseService, app: Application, fileStorage: FileStorage) {
    authenticate("auth-eduCo") {
        post("/api/user/tutor/update_course") {
            val courseId = call.parameters[QueryParams.PARAM_COURSE_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val multipart = call.receiveMultipart()
            var courseRequest:CourseRequest? = null
            var imageurl: String? = null

            multipart.forEachPart {partData ->
                when(partData) {
                    is PartData.FormItem -> {
                        if (partData.name == "course_data") {
                            courseRequest = Json.decodeFromString<CourseRequest>(partData.value)
                        }
                    }
                    is PartData.FileItem -> {

                        imageurl = fileStorage.saveToAWSBucket(
                            file = partData.toFile(),
                            bucketName = "prof-pictures",
                        )
                        app.log.info("Image Url Successfully Saved $imageurl")

                    }

                    else -> Unit
                }
                partData.dispose

            }

            courseRequest?.let { request ->
                val updateAcknowledged = courseService.updateCourseInfo(
                    courseId = courseId,
                    imageUrl = if (imageurl == null) {
                        null
                    } else {
                        imageurl
                    },
                    courseRequest = request
                )
                if (updateAcknowledged==true) {
                    app.log.info("course Info Successfully Update : $updateAcknowledged")
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true,
                        )
                    )
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }





        }
    }
}

fun Route.deleteCourse(courseService: CourseService) {
    authenticate("auth-eduCo") {
        delete("/api/user/tutor/delete_course") {
            val courseId = call.parameters[QueryParams.PARAM_COURSE_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val isDeleted = courseService.deleteCourse(courseId)
            if (!isDeleted) {
                call.respond(HttpStatusCode.Conflict)
            }
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(successful = isDeleted)
            )
        }
    }
}

fun Route.enrollCourse(courseService: CourseService, app:Application) {
    authenticate("auth-eduCo") {
        post("/api/user/enroll_course") {
            val courseId = call.parameters[QueryParams.PARAM_COURSE_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val userId = call.parameters[QueryParams.PARAM_USER_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val isEnrolled = courseService.enrollCourse(courseId, userId, app)
            app.log.info(isEnrolled.toString())
            if (isEnrolled) {
                call.respond(HttpStatusCode.OK)
            }
            else {
                call.respond(HttpStatusCode.Conflict)
                return@post
            }

        }
    }
}

fun Route.rateCourse(courseService: CourseService, app: Application) {
    authenticate("auth-eduCo") {
        post("/api/user/rate_course") {
            val courseId = call.parameters[QueryParams.PARAM_COURSE_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val userId=call.parameters[QueryParams.PARAM_USER_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val request = call.receiveOrNull<RatingRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            app.log.info("${request.ratingValue}")

            val isEnrolled = courseService.hasEnrolledCourse(courseId, userId)
            app.log.info("$isEnrolled")
            if (isEnrolled) {
                val isRatingSuccessful = courseService.rateCourse(courseId, userId, request.ratingValue, app)
                app.log.info("$isRatingSuccessful")
                if (isRatingSuccessful) {
                    call.respond(HttpStatusCode.OK)
                }
                else {
                    call.respond(HttpStatusCode.Conflict)
                    return@post
                }
            }
            else {
                call.respond(HttpStatusCode.Conflict,
                    message = "User has not Enrolled the Course@$courseId"
                )
                return@post
            }

        }
    }
}