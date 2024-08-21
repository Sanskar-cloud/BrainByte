package com.example.data.repository.like

import com.example.data.models.Like
import com.example.util.Constants
import io.ktor.server.application.*


interface LikeRepository {

    suspend fun likeParent(userId: String, parentId: String, parentType: Int, app: Application): Boolean

    suspend fun unlikeParent(userId: String, parentId: String, parentType: Int): Boolean

    suspend fun deleteLikesForParent(parentId: String)

    suspend fun getLikesForParent(
        parentId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_ACTIVITY_PAGE_SIZE
    ): List<Like>
}