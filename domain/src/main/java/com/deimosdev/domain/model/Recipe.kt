package com.deimosdev.domain.model

data class Recipe(
    var pk: Int,
    var title: String,
    var publisher: String? = null,
    var featuredImage: String,
    var rating: Int = 0,
    var sourceUrl: String? = null,
    var ingredients: List<String> = emptyList(),
)