package com.deimosdev.ui.recipe

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deimosdev.domain.model.Recipe
import com.deimosdev.domain.repository.RemoteRecipeRepository
import com.deimosdev.domain.util.DataStatusResponse
import com.deimosdev.domain.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val recipeRepository: RemoteRecipeRepository) :
    ViewModel() {
    val recipe: MutableState<Recipe?> = mutableStateOf(null)

    private val showError = mutableStateOf(false)

    val loading = mutableStateOf(false)

    val isDark = mutableStateOf(false)

    var recipeResponse: MutableState<DataStatusResponse<Recipe>> =
        mutableStateOf(DataStatusResponse.loading(null))

    fun onTriggerEvent(event: RecipeStateEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is RecipeStateEvent.GetRecipeEvent -> {
                        if (recipe.value == null) {
                            getRecipe(event.id)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private suspend fun getRecipe(id: Int) {
        loading.value = true

        // simulate a delay to show loading
       // delay(1000)

        recipeResponse.value = recipeRepository.getRecipeById(id = id)
        when (recipeResponse.value.status) {
            Status.SUCCESS -> this.recipe.value = recipeResponse.value.data
            Status.ERROR -> this.showError.value = true
            else -> {}
        }
        loading.value = false
    }
}