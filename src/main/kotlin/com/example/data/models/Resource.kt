package com.example.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Resource(
    @SerialName("_id")
    val resourceId: String = ObjectId().toString(),
    val lessonId:String?=null,
    val courseId: String?=null,
    val resourceUrl: List<String>,
    val resourceName: List<String>,
    val resourceSize: List<Double>,
    val fileType:List<String>
)
