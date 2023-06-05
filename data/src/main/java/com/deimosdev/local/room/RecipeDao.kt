package com.deimosdev.local.room

import androidx.room.Dao
import androidx.room.Query
import com.deimosdev.model.RecipeDto
import javax.inject.Singleton

@Dao
@Singleton
interface RecipeDao: BaseDao<RecipeDto> {
    @Query("SELECT * FROM Recipe")
    suspend fun getAllRecipes(): List<RecipeDto>

    @Query("SELECT * FROM Recipe WHERE pk = :pk")
    suspend fun getRecipeById(pk: Int): RecipeDto?

    @Query("DELETE FROM Recipe")
    suspend fun deleteAllRecipes()

}