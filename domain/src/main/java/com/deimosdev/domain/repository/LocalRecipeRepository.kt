package com.deimosdev.domain.repository

import com.deimosdev.domain.model.Recipe

interface LocalRecipeRepository {
    suspend fun getAllRecipes(): List<Recipe>
    suspend fun getRecipeById(recipeId: Int): Recipe?
    suspend fun insertRecipe(recipe: List<Recipe>){}
    suspend fun updateRecipe(recipe: Recipe){}
    suspend fun deleteRecipe(recipe: Recipe){}
}