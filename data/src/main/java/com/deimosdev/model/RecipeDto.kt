package com.deimosdev.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.deimosdev.utils.StringListConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Recipe")
@TypeConverters(StringListConverter::class)
data class RecipeDto (
    @PrimaryKey
    var pk: Int,
    var title: String,
    var publisher: String? = null,
    @SerializedName("featured_image")
    var featuredImage: String,
    var rating: Int = 0,
    @SerializedName("source_url")
    var sourceUrl: String? = null,
    var ingredients: List<String> = emptyList(),
)