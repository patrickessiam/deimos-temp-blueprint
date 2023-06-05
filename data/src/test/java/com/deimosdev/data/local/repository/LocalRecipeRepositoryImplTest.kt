package com.deimosdev.data.local.repository

import com.deimosdev.local.repository.LocalRecipeRepositoryImpl
import com.deimosdev.local.room.RecipeDao
import com.deimosdev.mapper.RecipeDtoMapper
import com.deimosdev.model.RecipeDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class LocalRecipeRepositoryImplTest {

    @Mock
    lateinit var recipeDao: RecipeDao

    @Mock
    lateinit var mapper: RecipeDtoMapper


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(
            this
        )
    }

    @Test
    fun `getAllRecipes empty response should return emptyList`() = runTest {
        Mockito.`when`(recipeDao.getAllRecipes())
            .thenReturn(
                emptyList()
            )

        val repository = LocalRecipeRepositoryImpl(recipeDao, mapper)
        val result = repository.getAllRecipes()
        assertThat(result.size).isEqualTo(0)
    }

    @Test
    fun `getAllRecipes should return all Locally Stored Recipes`() = runTest {
        val recipeItem1 = RecipeDto(
            pk = 1,
            title = "Test Recipe",
            featuredImage = "https://test.com/recipe.jpg",
            rating = 4,
            publisher = "Test Publisher",
            sourceUrl = "https://test.com/recipe",
            ingredients = listOf("Ingredient 1", "Ingredient 2")
        )
        val recipeItem3 = RecipeDto(
            pk = 3,
            title = "Test Recipe3",
            featuredImage = "https://test.com/recipe.jpg",
            rating = 4,
            publisher = "Test Publisher",
            sourceUrl = "https://test.com/recipe",
            ingredients = listOf("Ingredient 1", "Ingredient 2")
        )
        Mockito.`when`(recipeDao.getAllRecipes())
            .thenReturn(
                listOf(recipeItem1, recipeItem3)
            )

        val repository = LocalRecipeRepositoryImpl(recipeDao, mapper)
        val result = repository.getAllRecipes()
        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `getRecipeById should return the recipe if it exists`() = runTest {
        val recipeItem1 = RecipeDto(
            pk = 2,
            title = "Test Recipe",
            featuredImage = "https://test.com/recipe.jpg",
            rating = 4,
            publisher = "Test Publisher",
            sourceUrl = "https://test.com/recipe",
            ingredients = listOf("Ingredient 1", "Ingredient 2")
        )

        Mockito.`when`(recipeDao.getRecipeById(2))
            .thenReturn(
                recipeItem1
            )

        val repository = LocalRecipeRepositoryImpl(recipeDao, mapper)
        val result = repository.getRecipeById(1)
        assertThat(result).isNull()
    }
}