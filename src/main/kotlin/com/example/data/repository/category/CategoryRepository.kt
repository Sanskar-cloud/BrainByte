package com.example.data.repository.category

import com.example.data.models.Category
import com.example.data.models.CategoryOverview
import com.example.data.requests.CategoryRequest
import com.example.util.Constants


interface CategoryRepository {

    suspend fun getPopularCategories(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): List<CategoryOverview>?
    suspend fun createCategory(
        categoryRequest: CategoryRequest
    ): Boolean
    suspend fun updateCategory(
        categoryId: String,
        categoryRequest: CategoryRequest
    ): Boolean
    suspend fun deleteCategory(
        categoryId: String
    ): Boolean
}