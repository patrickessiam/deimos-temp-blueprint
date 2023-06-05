package com.deimosdev.domain.repository

import com.deimosdev.domain.model.Recipe
import com.deimosdev.domain.util.DataStatusResponse

interface RemoteRecipeRepository {
    suspend fun searchRecipe( page: Int, query: String): DataStatusResponse<List<Recipe>>
    suspend fun getRecipeById( id: Int): DataStatusResponse<Recipe>
}
