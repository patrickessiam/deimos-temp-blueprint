package com.deimosdev.di

import com.deimosdev.remote.RecipeService
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.deimosdev.frameworkutils.storage.SecureStorageHelper
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://food2fork.ca/api/recipe/"

    @Singleton
    @Provides
    fun provideHttpClient(storageHelper: SecureStorageHelper): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            val token = storageHelper.retrieveString(SecureStorageHelper.TOKEN_KEY).toString()

            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Token $token").build()
            chain.proceed(newRequest)
        }.build()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @Provides
    fun provideStorageModule(appContext: Application): SecureStorageHelper {
        return SecureStorageHelper(appContext)
    }

    @Singleton
    @Provides
    fun provideRecipeService(httpClient: OkHttpClient): RecipeService {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(httpClient).build().create(RecipeService::class.java)
    }
}
