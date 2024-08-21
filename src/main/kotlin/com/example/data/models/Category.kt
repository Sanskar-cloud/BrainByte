package com.example.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Category(
    @SerialName("_id")
    
    val id: String = ObjectId().toString(),
    val categoryImageUrl: String,
    val categoryName: String
)
