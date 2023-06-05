package com.deimosdev.data.di

import com.deimosdev.di.RepositoryModule
import com.deimosdev.local.room.RecipeDao
import com.deimosdev.local.room.RecipeDatabase
import com.deimosdev.mapper.RecipeDtoMapper
import com.deimosdev.remote.RecipeService
import com.deimosdev.remote.repository.RemoteRecipeRepositoryImpl
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RepositoryModuleTest {

    @Mock
    lateinit var db: RecipeDatabase

    @Mock
    lateinit var recipeMapper: RecipeDtoMapper

    @Mock
    lateinit var retrofitService: RecipeService

    @InjectMocks
    lateinit var repositoryModule: RepositoryModule

    @Test
    fun `provided RecipeRepository instance is RemoteRecipeRepositoryImpl`() {
        val dao = mock(RecipeDao::class.java)
        `when`(db.dao).thenReturn(dao)
        val expectedRepository = RemoteRecipeRepositoryImpl(dao, recipeMapper, retrofitService)
        val actualRepository =
            repositoryModule.provideRecipeRepository(db, recipeMapper, retrofitService)
        assertThat(actualRepository).isInstanceOf(expectedRepository::class.java)
    }

    @Test
    fun `provided RecipeDtoMapper instance is RecipeDtoMapper`() {
        val actualMapper = repositoryModule.provideRecipeDtoMapper()
        assertThat(actualMapper).isInstanceOf(RecipeDtoMapper::class.java)
    }
}
