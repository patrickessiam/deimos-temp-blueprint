package com.deimosdev.remote

import com.deimosdev.remote.response.RecipeSearchResponse
import com.deimosdev.model.RecipeDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeService {

    @GET("search")
    suspend fun search(
        @Query("page") page: Int, @Query("query") query: String
    ): Response<RecipeSearchResponse>

    @GET("get")
    suspend fun getRecipeById(
        @Query("id") id: Int
    ): Response<RecipeDto>
}











