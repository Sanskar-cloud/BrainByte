package com.example.data.repository.user

import com.example.data.models.User
import com.example.data.requests.UpdateProfileRequest
import com.example.util.Constants
import io.ktor.server.application.*



interface UserRepository {

    suspend fun getUserInfos(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): List<User>?

    suspend fun createUser(user: User)

    suspend fun deleteUser(userId: String): Boolean

    suspend fun getUserById(id: String): User?

    suspend fun getUserByEmail(email: String): User?

    suspend fun updateUser(
        userId: String,
        profileImageUrl: String?,
        bannerUrl: String?,
        updateProfileRequest: UpdateProfileRequest?,
        app: Application
    ): Boolean

    suspend fun updatePasswordForUser(userId: String, password: String, salt: String): Boolean
    suspend fun doesPasswordForUserMatch(email: String, enteredPassword: String): Boolean

    suspend fun doesEmailBelongToUserId(email: String, userId: String): Boolean

    suspend fun searchForUsers(query: String): List<User>

    suspend fun getUsers(userIds: List<String>): List<User>
}