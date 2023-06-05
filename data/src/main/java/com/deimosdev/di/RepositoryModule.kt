package com.deimosdev.di

import com.deimosdev.remote.RecipeService
import com.deimosdev.domain.repository.RemoteRecipeRepository
import com.deimosdev.mapper.RecipeDtoMapper
import com.deimosdev.remote.repository.RemoteRecipeRepositoryImpl
import com.deimosdev.local.room.RecipeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRecipeRepository(
        db: RecipeDatabase,
        recipeMapper: RecipeDtoMapper,
        retrofitService: RecipeService
    ): RemoteRecipeRepository {
        return RemoteRecipeRepositoryImpl(
            dao = db.dao,
            mapper = recipeMapper,
            retrofitService = retrofitService
        )
    }

    @Provides
    fun provideRecipeDtoMapper(): RecipeDtoMapper {
        return RecipeDtoMapper()
    }

}
