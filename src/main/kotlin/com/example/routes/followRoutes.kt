package com.example.routes

import com.example.data.models.Activity
import com.example.data.responses.BasicApiResponse
import com.example.data.util.ActivityType
import com.example.service.ActivityService
import com.example.service.FollowService
import com.example.util.QueryParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*


fun Route.followUser(
    followService: FollowService,
    activityService: ActivityService,
    app: Application
) {
    authenticate("auth-eduCo") {
        post("/api/user/follow") {
            val followedUserId = call.parameters[QueryParams.PARAM_USER_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val doesUserFollowing = followService.doesUserFollows(call.userId, followedUserId)
            if (doesUserFollowing) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = "this user has already been followed by you"
                    )
                )
                return@post
            }

            val isFollowed = followService.followUserIfExists(followedUserId, call.userId, app)

            if(isFollowed) {
                activityService.createActivity(
                    Activity(
                        timestamp = System.currentTimeMillis(),
                        byUserId = call.userId,
                        toUserId = followedUserId,
                        type = ActivityType.FollowedUser.type,
                        parentId = ""
                    )
                )
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = true
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = "user does not exists"
                    )
                )
            }
        }
    }

}

fun Route.unfollowUser(followService: FollowService) {
    authenticate("auth-eduCo") {
        delete("/api/user/unfollow") {
            val followedUserId = call.parameters[QueryParams.PARAM_USER_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val doesUserFollowing = followService.doesUserFollows(call.userId, followedUserId)
            if (doesUserFollowing) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = "this user has already been unfollowed by you"
                    )
                )
                return@delete
            }

            val didUserExist = followService.unfollowUserIfExists(followedUserId, call.userId)
            if(didUserExist) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = true
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = "user does not exists"
                    )
                )
            }
        }
    }
}