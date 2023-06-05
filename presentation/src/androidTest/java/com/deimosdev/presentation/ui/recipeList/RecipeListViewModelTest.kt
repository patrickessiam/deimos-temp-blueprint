package com.deimosdev.presentation.ui.recipeList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.deimosdev.domain.util.Status
import com.deimosdev.frameworkutils.storage.SecureStorageHelper
import com.deimosdev.local.repository.FakeRemoteRecipeRepositoryImpl
import com.deimosdev.presentation.MainCoroutineRule
import com.deimosdev.ui.recipeList.RecipeListEvent
import com.deimosdev.ui.recipeList.RecipeListViewModel
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
class RecipeListViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    private lateinit var viewModel: RecipeListViewModel
    private lateinit var fakeRepository: FakeRemoteRecipeRepositoryImpl
    private lateinit var secureStorage: SecureStorageHelper

    @Before
    fun setup() {
        fakeRepository = FakeRemoteRecipeRepositoryImpl()
        secureStorage = SecureStorageHelper(ApplicationProvider.getApplicationContext())
        viewModel = RecipeListViewModel(fakeRepository, null, secureStorage)
    }

    @Test
    fun newSearchDefaultStateIsLoading() = runBlockingTest {
        val expectedLoading = false
        viewModel.onTriggerEvent(RecipeListEvent.NewSearchEvent)
        val loading = viewModel.loading.value
        assertThat(expectedLoading).isEqualTo(loading)
    }

    @Test
    fun successfulNewSearchShouldReturnSuccessState() {
        viewModel.onTriggerEvent(RecipeListEvent.NewSearchEvent)
        val value = viewModel.recipeResults.value
        assertThat(value.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun failedNewSearchResponseShouldReturnErrorState() {
        fakeRepository.shouldReturnNetworkError(true)
        viewModel.onTriggerEvent(RecipeListEvent.NewSearchEvent)

        val value = viewModel.recipeResults.value

        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun successfulNextPageFetchShouldReturnSuccessState() {
        viewModel.onTriggerEvent(RecipeListEvent.NextPageEvent)
        val value = viewModel.recipeResults.value
        assertThat(value.status).isEqualTo(Status.SUCCESS)
    }
}