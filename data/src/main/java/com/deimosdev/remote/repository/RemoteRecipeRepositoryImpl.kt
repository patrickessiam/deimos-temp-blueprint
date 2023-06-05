package com.deimosdev.remote.repository

import com.deimosdev.domain.model.Recipe
import com.deimosdev.domain.repository.RemoteRecipeRepository
import com.deimosdev.domain.util.DataStatusResponse
import com.deimosdev.domain.util.Status
import com.deimosdev.local.room.RecipeDao
import com.deimosdev.mapper.RecipeDtoMapper
import com.deimosdev.remote.RecipeService

open class RemoteRecipeRepositoryImpl(
    private val dao: RecipeDao,
    private val mapper: RecipeDtoMapper,
    private val retrofitService: RecipeService
) : RemoteRecipeRepository {

    override suspend fun getRecipeById(id: Int): DataStatusResponse<Recipe> {
        try {
            val result = retrofitService.getRecipeById(id)
            return if (result.isSuccessful) {
                val recipeDto = result.body()
                val mappedRecipe = recipeDto?.let { mapper.mapToDomainModel(it) }
                DataStatusResponse(
                    status = Status.SUCCESS,
                    data = mappedRecipe,
                    message = null
                )
            } else {
                DataStatusResponse(
                    status = Status.ERROR,
                    data = null,
                    message = result.message()
                )
            }
        } catch (e: Exception) {
            return DataStatusResponse(
                status = Status.ERROR,
                data = null,
                message = e.message
            )
        }

    }

    override suspend fun searchRecipe(page: Int, query: String): DataStatusResponse<List<Recipe>> {
        val result = retrofitService.search(page = page, query = query)
        return if (result.isSuccessful) {
            val recipeDto = result.body()
            recipeDto?.let { dao.insert(it.recipes) }
            val mappedRecipe = recipeDto?.let { mapper.toDomainList(it.recipes) }
            DataStatusResponse(
                status = Status.SUCCESS,
                data = mappedRecipe,
                message = null
            )
        } else {
            DataStatusResponse(
                status = Status.ERROR,
                data = null,
                message = result.message()
            )
        }
    }
}