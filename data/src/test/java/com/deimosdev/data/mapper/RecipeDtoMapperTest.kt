package com.deimosdev.data.mapper

import com.deimosdev.domain.model.Recipe
import com.deimosdev.mapper.RecipeDtoMapper
import com.deimosdev.model.RecipeDto
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class RecipeDtoMapperTest {
    private lateinit var recipeDtoMapper:RecipeDtoMapper

    private val recipeDto = RecipeDto(
        pk = 1,
        title = "Test Recipe",
        featuredImage = "https://test.com/recipe.jpg",
        rating = 4,
        publisher = "Test Publisher",
        sourceUrl = "https://test.com/recipe",
        ingredients = listOf("Ingredient 1", "Ingredient 2")
    )

    private val recipe = Recipe(
        pk = 1,
        title = "Test Recipe",
        featuredImage = "https://test.com/recipe.jpg",
        rating = 4,
        publisher = "Test Publisher",
        sourceUrl = "https://test.com/recipe",
        ingredients = listOf("Ingredient 1", "Ingredient 2")
    )

    @Before
    fun setUp() {
        recipeDtoMapper = RecipeDtoMapper()
    }

    @Test
    fun `mapToDomainModel should return the expected Recipe object`() {
        val result = recipeDtoMapper.mapToDomainModel(recipeDto)
        assertThat(result).isEqualTo(recipe)
    }

    @Test
    fun `mapFromDomainModel should return the expected RecipeDto object`() {
        val result = recipeDtoMapper.mapFromDomainModel(recipe)
        assertThat(result).isEqualTo(recipeDto)
    }

    @Test
    fun `toDomainList should return the expected List of Recipe objects`() {
        val recipeDtoList = listOf(recipeDto, recipeDto.copy(pk = 2))
        val result = recipeDtoMapper.toDomainList(recipeDtoList)
        assertThat(result).isEqualTo(listOf(recipe, recipe.copy(pk = 2)))
    }

    @Test
    fun `fromDomainList should return the expected List of RecipeDto objects`() {
        val recipeList = listOf(recipe, recipe.copy(pk = 2))
        val result = recipeDtoMapper.fromDomainList(recipeList)
        assertThat(result).isEqualTo(listOf(recipeDto, recipeDto.copy(pk = 2)))
    }
}
