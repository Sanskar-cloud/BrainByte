package com.example.data.repository.user


import com.example.data.models.User
import com.example.data.models.UserType
import com.example.data.requests.UpdateProfileRequest
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.Document


class UserRepositoryImpl(db: MongoDatabase): UserRepository {

    private val users = db.getCollection<User>("User")
    override suspend fun getUserInfos(page: Int, pageSize: Int): List<User> {
        val userItems = users.find()
            .skip(page*pageSize)
            .limit(pageSize)
            .toList()
            /*.map { user ->
                val isFollowing = followsByUser.find { user.followedUserId == user.id } != null
                UserResponseItem(
                    userId = user.id,
                    profilePictureUrl = user.profileImageUrl,
                    username = user.username,
                    bio = user.bio,
                    name = user.profileName?: "Empty Name",
                    isFollowing =
                )
            }*/
        println(userItems)
        return userItems

    }

    override suspend fun createUser(user: User) {
        users.insertOne(user)
    }

    override suspend fun deleteUser(userId: String): Boolean {
        return users.deleteOne(Filters.eq("id",userId)).wasAcknowledged()
    }

    override suspend fun getUserById(id: String): User? {
        println("Querying user with ID: $id")
        val user = users.find(Filters.eq("_id", id)).firstOrNull()

        if (user != null) {
            println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAUser found:")
            println("Name: ${user.username}, ID: ${user.id}")
        } else {
            println("User not found for ID: $id")
        }

        return user


    }

    override suspend fun getUserByEmail(email: String): User? {
        return users.find(Filters.eq("email",email)).firstOrNull()
    }

    override suspend fun updateUser(
        userId: String,
        profileImageUrl: String?,
        bannerUrl: String?,
        updateProfileRequest: UpdateProfileRequest?,
        app: Application,
    ): Boolean {
        val user = getUserById(userId) ?: return false
        return users.updateOne(
            Filters.eq("_id",userId),
            Updates.combine(
                Updates.set("username",updateProfileRequest?.username),
                Updates.set("facebookUrl",updateProfileRequest?.facebookUrl),
                Updates.set("bio",updateProfileRequest?.bio),
                Updates.set("profileImageUrl",profileImageUrl?: user.profileImageUrl),
                Updates.set("bannerUrl",bannerUrl?: user.bannerUrl),
                Updates.set("faceBookUrl",updateProfileRequest?.facebookUrl),
                Updates.set("instagramUrl", updateProfileRequest?.instagramUrl),
                Updates.set("twitterUrl", updateProfileRequest?.twitterUrl)







            )

        ).wasAcknowledged()


    }

    override suspend fun updatePasswordForUser(userId: String, password: String, salt: String): Boolean {
        return users.updateOne(
            Filters.eq("id",userId),
            Updates.combine(
                Updates.set("password",password),
                Updates.set("salt",salt)
            )

        ).wasAcknowledged()
    }


    override suspend fun doesPasswordForUserMatch(email: String, enteredPassword: String): Boolean {
        val user = getUserByEmail(email)
        return user?.password == enteredPassword
    }

    override suspend fun doesEmailBelongToUserId(email: String, userId: String): Boolean {
        val user = users.find(Filters.eq("id", userId)).firstOrNull()
        if (user != null) {
            if(user.email==email){
                return true

            }
            else{
                return false
            }
        }
        else{
            return false
        }
//        if(user!=null){
//            return (user as?Document)?.getString("email") == email
//        }
//
//        return (user as? Document)?.getString("email") == email
//        return users.findOneById(userId)?.email == email
    }

    override suspend fun searchForUsers(query: String): List<User> {
        return users.find(
            Filters.or(
                Filters.regex("username", ".*$query.*", "i"),
                Filters.eq("email", query)
            )


        ).toList()
    }

    override suspend fun getUsers(userIds: List<String>): List<User> {
        return users.find(Filters.`in`("id",userIds)).toList()
    }
}