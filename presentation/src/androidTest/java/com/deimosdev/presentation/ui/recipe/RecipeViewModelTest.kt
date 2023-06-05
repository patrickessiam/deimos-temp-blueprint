package com.deimosdev.presentation.ui.recipe

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.deimosdev.domain.util.Status
import com.deimosdev.local.repository.FakeRemoteRecipeRepositoryImpl
import com.deimosdev.presentation.MainCoroutineRule
import com.deimosdev.ui.recipe.RecipeStateEvent.GetRecipeEvent
import com.deimosdev.ui.recipe.RecipeViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RecipeViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    private lateinit var viewModel: RecipeViewModel
    private lateinit var fakeRepository: FakeRemoteRecipeRepositoryImpl

    @Before
    fun setup() {
        fakeRepository = FakeRemoteRecipeRepositoryImpl()
        viewModel = RecipeViewModel(fakeRepository)
    }

    @Test
    fun getRecipeDefaultStateIsLoading() = runBlockingTest {
        val expectedLoading = false
        viewModel.onTriggerEvent(GetRecipeEvent(1))
        val loading = viewModel.loading.value
        assertThat(expectedLoading).isEqualTo(loading)
    }

    @Test
    fun successfulGetRecipeShouldReturnSuccessState() = runBlockingTest {
        viewModel.recipe.value = null
        viewModel.onTriggerEvent(GetRecipeEvent(1))
        val value = viewModel.recipeResponse.value
        assertThat(value.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun failedGetRecipeResponseShouldReturnErrorState() = runBlockingTest {
        fakeRepository.shouldReturnNetworkError(true)
        viewModel.recipe.value = null
        viewModel.onTriggerEvent(GetRecipeEvent(1))
        val value = viewModel.recipeResponse.value
        assertThat(value.status).isEqualTo(Status.ERROR)
    }
}