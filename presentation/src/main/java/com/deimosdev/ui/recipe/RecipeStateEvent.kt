package com.deimosdev.ui.recipe

sealed class RecipeStateEvent {

    data class GetRecipeEvent(
        val id: Int
    ) : RecipeStateEvent()
}
