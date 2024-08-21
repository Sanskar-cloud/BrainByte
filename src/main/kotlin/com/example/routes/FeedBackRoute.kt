package com.example.routes

import com.example.data.requests.FeedbackRequest
import com.example.data.responses.BasicApiResponse
import com.example.service.FeedBackService
import com.example.util.QueryParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.createFeedBack(feedBackService: FeedBackService) {

    authenticate("auth-eduCo") {
        post("/api/course/feedback/create") {
            val courseId = call.parameters[QueryParams.PARAM_COURSE_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val request = call.receiveOrNull<FeedbackRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val isCreated = feedBackService.createFeedback(
                call.userId,
                courseId,
                request
            )

            if (!isCreated) {
                call.respond(
                    HttpStatusCode.Conflict
                )
            }
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit> (successful = isCreated)
            )
        }
    }
}

fun Route.updateFeedBack(feedBackService: FeedBackService) {

    authenticate("auth-eduCo") {
        post("/api/course/feedback/update") {
            val feedbackId = call.parameters[QueryParams.PARAM_FEEDBACK_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val request = call.receiveOrNull<FeedbackRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val isUpdated = feedBackService.updateFeedback(
                feedbackId,
                request
            )

            if (!isUpdated) {
                call.respond(
                    HttpStatusCode.Conflict
                )
            }
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit> (successful = isUpdated)
            )
        }
    }
}

fun Route.deleteFeedBack(feedBackService: FeedBackService) {

    authenticate("auth-eduCo") {
        post("/api/course/feedback/delete") {
            val feedbackId = call.parameters[QueryParams.PARAM_FEEDBACK_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val isDeleted = feedBackService.deleteFeedback(
                feedbackId,
            )

            if (!isDeleted) {
                call.respond(
                    HttpStatusCode.Conflict
                )
            }
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit> (successful = isDeleted)
            )
        }
    }
}

fun Route.deleteAllFeedBack(feedBackService: FeedBackService) {

    authenticate("auth-eduCo") {
        post("/api/course/feedback/delete_all") {
            val courseId = call.parameters[QueryParams.PARAM_COURSE_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val isDeleted = feedBackService.deleteAllFeedback(
                courseId
            )

            if (!isDeleted) {
                call.respond(
                    HttpStatusCode.Conflict
                )
            }
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit> (successful = isDeleted)
            )
        }
    }
}

