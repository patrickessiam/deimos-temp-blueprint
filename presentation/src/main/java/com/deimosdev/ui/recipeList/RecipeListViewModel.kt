package com.deimosdev.ui.recipeList

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.deimosdev.domain.model.Recipe
import com.deimosdev.domain.repository.RemoteRecipeRepository
import com.deimosdev.frameworkutils.storage.SecureStorageHelper
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.deimosdev.domain.util.DataStatusResponse
import com.deimosdev.domain.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers

const val PAGE_SIZE = 30

@HiltViewModel
class RecipeListViewModel
@Inject constructor(
    private val repository: RemoteRecipeRepository,
    private val remoteConfig: FirebaseRemoteConfig?,
    private val secureStorage: SecureStorageHelper
) : ViewModel() {

    val recipes: MutableState<List<Recipe>> = mutableStateOf(ArrayList())

    val fabEnabled: MutableState<Boolean> = mutableStateOf(false)

    val query = mutableStateOf("")

    val loading = mutableStateOf(false)

    val isDark = mutableStateOf(false)

    val showError = mutableStateOf(false)

    val error = mutableStateOf("")

    // Pagination starts at '1'
    val page = mutableStateOf(1)

    var recipeListScrollPosition = 0

    var recipeResults: MutableState<DataStatusResponse<List<Recipe>>> =
        mutableStateOf(DataStatusResponse.loading(null))

    init {
        onTriggerEvent(RecipeListEvent.NewSearchEvent)
    }

    fun onTriggerEvent(event: RecipeListEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is RecipeListEvent.NewSearchEvent -> {
                        newSearch()
                    }
                    is RecipeListEvent.NextPageEvent -> {
                        nextPage()
                    }
                    is RecipeListEvent.FABEvent -> {
                        enableFAB()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun newSearch() {
        loading.value = true

        resetSearchState()

        val result = repository.searchRecipe(
            page = 1,
            query = query.value
        )
        recipeResults.value = result
        when (result.status) {
            Status.SUCCESS -> this.recipes.value = result.data!!
            Status.ERROR -> {
                this.showError.value = true
                this.error.value = result.message!!

            }
            else -> {}
        }

        loading.value = false
    }

    private suspend fun nextPage() {
        // prevent duplicate event due to recompose happening to quickly
        if ((recipeListScrollPosition + 1) >= (page.value * PAGE_SIZE)) {
            loading.value = true
            incrementPage()
            // just to show pagination, api is fast
            delay(1000)

            if (page.value > 1) {
                val result = repository.searchRecipe(page = page.value, query = query.value)
                appendRecipes(result.data!!)
                Log.d(TAG, "search: appending")
                recipeResults.value = result
                when (result.status) {
                    Status.SUCCESS -> appendRecipes(result.data!!)
                    Status.ERROR -> {
                        this.showError.value = true
                        this.error.value = result.message!!

                    }
                    else -> {}
                }
            }
            loading.value = false
        }
    }

    /**
     * Append new recipes to the current list of recipes
     */
    private fun appendRecipes(recipes: List<Recipe>) {
        val current = ArrayList(this.recipes.value)
        current.addAll(recipes)
        this.recipes.value = current
    }

    private fun enableFAB() {
        remoteConfig?.fetchAndActivate()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                this.fabEnabled.value = remoteConfig.getBoolean("fab_enabled")
            }
        }
    }

    private fun incrementPage() {
        setPage(page.value + 1)
    }

    fun onChangeRecipeScrollPosition(position: Int) {
        setListScrollPosition(position = position)
    }

    fun saveToken(token: String) = secureStorage.saveString(SecureStorageHelper.TOKEN_KEY,token)

    /**
     * Called when a new search is executed.
     */
    private fun resetSearchState() {
        recipes.value = listOf()
        page.value = 1
        onChangeRecipeScrollPosition(0)
    }

    fun onQueryChanged(query: String) {
        setQuery(query)
    }

    private fun setListScrollPosition(position: Int) {
        recipeListScrollPosition = position
    }

    private fun setPage(page: Int) {
        this.page.value = page
    }

    private fun setQuery(query: String) {
        this.query.value = query
    }

    fun toggleLightTheme(isDarkValue: Boolean) {
        isDark.value = !isDarkValue
    }
}
