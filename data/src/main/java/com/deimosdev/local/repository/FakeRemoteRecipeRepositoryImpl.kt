package com.deimosdev.local.repository

import com.deimosdev.domain.model.Recipe
import com.deimosdev.domain.repository.RemoteRecipeRepository
import com.deimosdev.domain.util.DataStatusResponse
import com.deimosdev.domain.util.Status

class FakeRemoteRecipeRepositoryImpl : RemoteRecipeRepository {

    private var shouldReturnNetworkError = false

    fun shouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun searchRecipe(page: Int, query: String): DataStatusResponse<List<Recipe>> {
        return if (shouldReturnNetworkError) {
            DataStatusResponse(Status.ERROR, null, "Error")
        } else {
            DataStatusResponse(Status.SUCCESS, listOf(), null)
        }
    }

    override suspend fun getRecipeById(id: Int): DataStatusResponse<Recipe> {
        return if (shouldReturnNetworkError) {
            DataStatusResponse(Status.ERROR, null, "Error")
        } else {
            DataStatusResponse(
                Status.SUCCESS,
                Recipe(
                    pk = 1,
                    title = "Test Recipe",
                    featuredImage = "https://test.com/recipe.jpg",
                    rating = 4,
                    publisher = "Test Publisher",
                    sourceUrl = "https://test.com/recipe",
                    ingredients = listOf("Ingredient 1", "Ingredient 2")
                ),
                null
            )
        }
    }
}