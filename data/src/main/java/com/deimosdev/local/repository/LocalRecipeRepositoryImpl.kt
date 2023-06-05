package com.deimosdev.local.repository

import com.deimosdev.domain.model.Recipe
import com.deimosdev.domain.repository.LocalRecipeRepository
import com.deimosdev.mapper.RecipeDtoMapper
import com.deimosdev.local.room.RecipeDao

class LocalRecipeRepositoryImpl(
    private val dao: RecipeDao,
    private val mapper: RecipeDtoMapper,
) : LocalRecipeRepository {
    override suspend fun getAllRecipes(): List<Recipe> {
        return mapper.toDomainList(dao.getAllRecipes())
    }

    override suspend fun getRecipeById(recipeId: Int): Recipe? {
        val recipeDto = dao.getRecipeById(recipeId)
        return recipeDto?.let { mapper.mapToDomainModel(it) }

    }


    override suspend fun insertRecipe(recipe: List<Recipe>) {
        dao.insert(mapper.fromDomainList(recipe))
    }

}