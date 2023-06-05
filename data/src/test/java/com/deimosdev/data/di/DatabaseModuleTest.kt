package com.deimosdev.data.di

import android.app.Application
import com.deimosdev.di.DatabaseModule
import com.deimosdev.local.room.RecipeDatabase
import com.google.common.truth.Truth.assertThat
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class DatabaseModuleTest {

    @Mock
    lateinit var application: Application

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun providedRecipeDatabaseIsNotNull() {
        val database = DatabaseModule.provideRecipeDatabase(application)
        assertThat(database).isNotNull()

    }

    @Test
    fun providedRecipeDatabaseReturnsInstanceOfRecipeDatabase() {
        val database = DatabaseModule.provideRecipeDatabase(application)
        assertThat(database).isInstanceOf(RecipeDatabase::class.java)

    }

    @Test
    fun providedRecipeDatabaseNameIsValid() {
        val database = DatabaseModule.provideRecipeDatabase(application)
        assertThat(RecipeDatabase.DATABASE_NAME).isEqualTo(database.openHelper.databaseName)
    }
}
