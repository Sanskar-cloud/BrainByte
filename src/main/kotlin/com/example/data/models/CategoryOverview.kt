package com.example.data.models

import kotlinx.serialization.Serializable

@Serializable
data class CategoryOverview (
    val categoryId:String,
    val categoryImageUrl: String,
    val categoryName: String

){

}