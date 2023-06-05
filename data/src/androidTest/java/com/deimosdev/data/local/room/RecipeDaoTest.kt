package com.deimosdev.data.local.room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.deimosdev.local.room.RecipeDao
import com.deimosdev.local.room.RecipeDatabase
import com.deimosdev.model.RecipeDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class RecipeDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RecipeDatabase
    private lateinit var dao: RecipeDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RecipeDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.dao

    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertRecipeItem() = runBlockingTest {
        val recipeItem = RecipeDto(
            pk = 1,
            title = "Test Recipe",
            featuredImage = "https://test.com/recipe.jpg",
            rating = 4,
            publisher = "Test Publisher",
            sourceUrl = "https://test.com/recipe",
            ingredients = listOf("Ingredient 1", "Ingredient 2")
        )
        dao.insert(recipeItem)

        val allRecipeItems = dao.getAllRecipes()

        assertThat(allRecipeItems).contains(recipeItem)
    }

    @Test
    fun deleteRecipeItem() = runBlockingTest {
        val recipeItem = RecipeDto(
            pk = 1,
            title = "Test Recipe",
            featuredImage = "https://test.com/recipe.jpg",
            rating = 4,
            publisher = "Test Publisher",
            sourceUrl = "https://test.com/recipe",
            ingredients = listOf("Ingredient 1", "Ingredient 2")
        )
        dao.insert(recipeItem)
        dao.delete(recipeItem)

        val allRecipeItems = dao.getAllRecipes()

        assertThat(allRecipeItems).doesNotContain(recipeItem)
    }

    @Test
    fun getRecipeById() = runBlockingTest {
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
        dao.upsert(listOf(recipeItem1, recipeItem3))

        val recipeByIdItem = dao.getRecipeById(3)

        assertThat(recipeByIdItem?.title).isEqualTo("Test Recipe3")
    }

    @Test
    fun getRecipeInsertedCount() = runBlockingTest {
        val recipeItem1 = RecipeDto(
            pk = 1,
            title = "Test Recipe",
            featuredImage = "https://test.com/recipe.jpg",
            rating = 4,
            publisher = "Test Publisher",
            sourceUrl = "https://test.com/recipe",
            ingredients = listOf("Ingredient 1", "Ingredient 2")
        )
        val recipeItem2 = RecipeDto(
            pk = 2,
            title = "Test Recipe2",
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
        dao.upsert(listOf(recipeItem1, recipeItem2, recipeItem3))

        val recipeCount = dao.getAllRecipes().count()

        assertThat(recipeCount).isEqualTo(3)
    }

    @Test
    fun deleteAllRecipes() = runBlockingTest {
        val recipeItem1 = RecipeDto(
            pk = 1,
            title = "Test Recipe",
            featuredImage = "https://test.com/recipe.jpg",
            rating = 4,
            publisher = "Test Publisher",
            sourceUrl = "https://test.com/recipe",
            ingredients = listOf("Ingredient 1", "Ingredient 2")
        )
        val recipeItem2 = RecipeDto(
            pk = 2,
            title = "Test Recipe2",
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
        dao.upsert(listOf(recipeItem1, recipeItem2, recipeItem3))
        dao.deleteAllRecipes()
        val recipeCount = dao.getAllRecipes().count()

        assertThat(recipeCount).isEqualTo(0)
    }

    @Test
    fun updateRecipeById() = runBlockingTest {
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
            pk = 1,
            title = "Test Recipe3",
            featuredImage = "https://test.com/recipe.jpg",
            rating = 4,
            publisher = "Test Publisher",
            sourceUrl = "https://test.com/recipe",
            ingredients = listOf("Ingredient 1", "Ingredient 2")
        )
        dao.insert(recipeItem1)
        dao.update(recipeItem3)

        val recipeByIdItem = dao.getRecipeById(1)

        assertThat(recipeByIdItem?.title).isEqualTo("Test Recipe3")
    }

    @Test
    fun upsertRecipeById() = runBlockingTest {
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
            pk = 1,
            title = "Test Recipe3",
            featuredImage = "https://test.com/recipe.jpg",
            rating = 4,
            publisher = "Test Publisher",
            sourceUrl = "https://test.com/recipe",
            ingredients = listOf("Ingredient 1", "Ingredient 2")
        )
        dao.insert(recipeItem1)
        dao.upsert(recipeItem3)

        val recipeByIdItem = dao.getRecipeById(1)

        assertThat(recipeByIdItem?.title).isEqualTo("Test Recipe3")
    }
}