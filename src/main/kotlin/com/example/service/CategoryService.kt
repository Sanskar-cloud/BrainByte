package com.example.service

import com.example.data.models.Category
import com.example.data.models.CategoryOverview
import com.example.data.repository.category.CategoryRepository
import com.example.data.requests.CategoryRequest
import com.example.util.Constants

class CategoryService(
    private val categoryRepository: CategoryRepository
) {

    suspend fun getPopularCategories(
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): List<CategoryOverview>? {
        return categoryRepository.getPopularCategories(page, pageSize)
    }

    suspend fun createCategory(
        categoryRequest: CategoryRequest
    ): Boolean {
        return categoryRepository.createCategory(categoryRequest)
    }

    suspend fun updateCategory(
        categoryId: String,
        categoryRequest: CategoryRequest
    ): Boolean {
        return categoryRepository.updateCategory(categoryId, categoryRequest)
    }

    suspend fun deleteCategory(
        categoryId: String
    ): Boolean {
        return categoryRepository.deleteCategory(categoryId)
    }
}