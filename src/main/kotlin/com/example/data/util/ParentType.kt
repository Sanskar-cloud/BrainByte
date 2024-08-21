package com.example.data.util

import com.example.data.requests.SendEmailRequestData
import com.google.gson.Gson

sealed class ParentType(val type: Int) {
    object Course : ParentType(0)
    object Comment : ParentType(1)
    object None : ParentType(2)

    companion object {
        fun fromType(type: Int): ParentType {
            return when(type) {
                0 -> Course
                1 -> Comment
                else -> None
            }
        }
    }
}


suspend fun ByteArray.toSendEmailRequestData(): SendEmailRequestData {
    val data = String(this, Charsets.UTF_8) // Convert ByteArray to String
    val gson = Gson()
    return gson.fromJson(data, SendEmailRequestData::class.java)
}

