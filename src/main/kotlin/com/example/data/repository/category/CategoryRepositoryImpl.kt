package com.example.data.repository.category

import com.example.data.models.Category
import com.example.data.models.CategoryOverview
import com.example.data.requests.CategoryRequest
import com.example.util.Constants
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList



class CategoryRepositoryImpl(
    db: MongoDatabase
): CategoryRepository {

    private val categories = db.getCollection<Category>("Category")
    override suspend fun getPopularCategories(page: Int, pageSize: Int): List<CategoryOverview>? {
        return categories.find()
            .skip(page * pageSize)
            .limit(pageSize)
            .sort(Sorts.descending("categoryName"))
            .toList()
            .map {category ->
                CategoryOverview(
                    categoryId  = category.id,
                    categoryName = category.categoryName,
                    categoryImageUrl = category.categoryImageUrl
                )
            }
    }

    override suspend fun createCategory(categoryRequest: CategoryRequest): Boolean {
        return categories.insertOne(
            Category(
                categoryImageUrl = categoryRequest.categoryImageUrl?: Constants.DEFAULT_BANNER_IMAGE_PATH,
                categoryName = categoryRequest.categoryName
            )
        ).wasAcknowledged()
    }

    override suspend fun updateCategory(categoryId: String, categoryRequest: CategoryRequest): Boolean {
        return categories.updateOne(
            Filters.eq("id", categoryId),
            Updates.combine(
                Updates.set("categoryName", categoryRequest.categoryName),
                Updates.set("categoryImageUrl", categoryRequest.categoryImageUrl)
            )


            ).wasAcknowledged()
    }

    override suspend fun deleteCategory(categoryId: String): Boolean {
        val result = categories.deleteOne(Filters.eq("id", categoryId))
        return result.wasAcknowledged()

    }
}