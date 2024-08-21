package com.example.data.models

import com.example.data.requests.AccountDetails
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName
import org.bson.types.ObjectId

@Serializable
data class User(
    @SerialName("_id")
    val id: String = ObjectId().toString(),

    val email: String,
    val username: String,
    val password: String,
    val salt: String,
    val userType: String?=null,
    val profileName: String? = null,
    val profileImageUrl: String,
    val bannerUrl: String?,
    val watchedCourses: MutableList<CourseOverview> = mutableListOf(),
    val bio: String,
    val faceBookUrl: String?,
    val instagramUrl: String?,
    val twitterUrl: String?,
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val courseCount: Int = 0,
    val totalProjectsCount: Int = 0,
    val accountDetails: AccountDetails? = null
)



/*fun main() {
    val apple = UserType.STUDENT
    val jsonString = Json.encodeToString(apple)
    println(jsonString)
}*/


