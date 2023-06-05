package com.deimosdev.ui.recipeList

sealed class RecipeListEvent {
    object NewSearchEvent : RecipeListEvent()
    object NextPageEvent : RecipeListEvent()
    object FABEvent : RecipeListEvent()

}