package com.deimosdev.data.remote.repository

import com.deimosdev.domain.util.Status
import com.deimosdev.local.room.RecipeDao
import com.deimosdev.mapper.RecipeDtoMapper
import com.deimosdev.model.RecipeDto
import com.deimosdev.remote.RecipeService
import com.deimosdev.remote.repository.RemoteRecipeRepositoryImpl
import com.deimosdev.remote.response.RecipeSearchResponse
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

class RemoteRecipeRepositoryImpTest {

    @Mock
    lateinit var recipeService: RecipeService

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
    fun `searchRecipe empty response should return emptyList`() = runTest {
        Mockito.`when`(recipeService.search(1, "hhgeygdyeggy"))
            .thenReturn(
                Response.success(RecipeSearchResponse(0, emptyList()))
            )

        val repository = RemoteRecipeRepositoryImpl(recipeDao, mapper, recipeService)
        val result = repository.searchRecipe(1, "hhgeygdyeggy")
        assertThat(result.data!!.size).isEqualTo(0)
    }

    @Test
    fun `searchRecipe should return expected Recipe list size`() = runTest {
        val recipeDto = RecipeDto(
            pk = 1,
            title = "Test Recipe",
            featuredImage = "https://test.com/recipe.jpg",
            rating = 4,
            publisher = "Test Publisher",
            sourceUrl = "https://test.com/recipe",
            ingredients = listOf("Ingredient 1", "Ingredient 2")
        )
        Mockito.`when`(recipeService.search(1, "test")).thenReturn(
            Response.success(
                RecipeSearchResponse(
                    2,
                    listOf(
                        recipeDto, recipeDto.copy(pk = 2)
                    )
                )
            )
        )

        val repository = RemoteRecipeRepositoryImpl(recipeDao, mapper, recipeService)
        val result = repository.searchRecipe(1, "test")
        assertThat(result.data!!.size).isEqualTo(2)
    }

    @Test
    fun `searchRecipe error response should return an error`() = runTest {
        Mockito.`when`(recipeService.search(1, "hhgeygdyeggy"))
            .thenReturn(
                Response.error(500, "Internal Server Error".toResponseBody())
            )

        val repository = RemoteRecipeRepositoryImpl(recipeDao, mapper, recipeService)
        val result = repository.searchRecipe(1, "hhgeygdyeggy")
        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `searchRecipe error response message should return an error message`() = runTest {
        Mockito.`when`(recipeService.search(1, "hhgeygdyeggy"))
            .thenReturn(
                Response.error(500, "Internal Server Error".toResponseBody())
            )

        val repository = RemoteRecipeRepositoryImpl(recipeDao, mapper, recipeService)
        val result = repository.searchRecipe(1, "hhgeygdyeggy")
        assertThat(result.message).isNotNull()
    }

    @Test
    fun `getRecipeById error response should return an error`() = runTest {
        Mockito.`when`(recipeService.getRecipeById(1))
            .thenReturn(
                Response.error(500, "Internal Server Error".toResponseBody())
            )

        val repository = RemoteRecipeRepositoryImpl(recipeDao, mapper, recipeService)
        val result = repository.getRecipeById(1)
        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `getRecipeById error response message should return an error message`() = runTest {
        Mockito.`when`(recipeService.getRecipeById(1))
            .thenReturn(
                Response.error(500, "Internal Server Error".toResponseBody())
            )

        val repository = RemoteRecipeRepositoryImpl(recipeDao, mapper, recipeService)
        val result = repository.getRecipeById(1)
        assertThat(result.message).isNotNull()
    }

}

