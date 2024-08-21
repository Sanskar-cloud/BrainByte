package com.example.routes

import com.example.data.requests.ProjectRequest
import com.example.data.responses.BasicApiResponse
import com.example.service.ProjectService
import com.example.util.QueryParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.getProject(projectService: ProjectService) {
    authenticate("auth-eduCo") {
        get("/api/course/project") {
            val projectId = call.parameters[QueryParams.PARAM_PROJECT_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val project = projectService.getProject(projectId)

            if (project == null) {
                call.respond(
                    HttpStatusCode.NotFound
                )
                return@get
            }

            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(successful = true, data = project)
            )
        }

    }
}

fun Route.getProjectsForCourse(projectService: ProjectService) {
    authenticate("auth-eduCo") {
        get("/api/user/course/projects") {
            val courseId = call.parameters[QueryParams.PARAM_COURSE_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val projects = projectService.getAllProject(courseId)

            projects?.let {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = BasicApiResponse(
                        successful = true,
                        data = projects
                    )
                )
            }?: kotlin.run {
                call.respond(
                    HttpStatusCode.NotFound,
                    message = "projects are not available"
                )
                return@get
            }
        }

    }
}

fun Route.createProject(projectService: ProjectService) {
    authenticate("auth-eduCo") {
        post("/api/course/project/create") {
            val courseId = call.parameters[QueryParams.PARAM_COURSE_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val request = call.receiveOrNull<ProjectRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val isCreated = projectService.createProject(courseId, request)

            if (!isCreated) {
                call.respond(
                    HttpStatusCode.Conflict
                )
                return@post
            }

            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(successful = true)
            )
        }

    }
}

fun Route.updateProject(projectService: ProjectService) {
    authenticate("auth-eduCo") {
        post("/api/course/project/update") {
            val projectId = call.parameters[QueryParams.PARAM_PROJECT_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val request = call.receiveOrNull<ProjectRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val isUpdated = projectService.updateProject(request, projectId)

            if (!isUpdated) {
                call.respond(
                    HttpStatusCode.Conflict
                )
                return@post
            }

            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(successful = true)
            )
        }

    }
}

fun Route.deleteProject(projectService: ProjectService) {
    authenticate("auth-eduCo") {
        delete("/api/course/project/delete") {
            val projectId = call.parameters[QueryParams.PARAM_PROJECT_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val isDeleted = projectService.deleteProject(projectId)

            if (!isDeleted) {
                call.respond(
                    HttpStatusCode.Conflict
                )
                return@delete
            }

            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(successful = true)
            )
        }

    }
}

fun Route.deleteAllProject(projectService: ProjectService) {
    authenticate("auth-eduCo") {
        delete("/api/course/project/delete_all") {
            val courseId = call.parameters[QueryParams.PARAM_COURSE_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val isDeleted = projectService.deleteAllProject(courseId)

            if (!isDeleted) {
                call.respond(
                    HttpStatusCode.Conflict
                )
                return@delete
            }

            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(successful = true)
            )
        }

    }
}