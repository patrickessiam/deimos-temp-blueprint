package com.deimosdev.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.deimosdev.model.RecipeDto

@Database(
    entities = [RecipeDto::class],
    version = 1
)
abstract class RecipeDatabase: RoomDatabase() {
    abstract val dao: RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null
        const val DATABASE_NAME = "template_database"

        fun getDatabase(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    DATABASE_NAME
                ).addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            db.execSQL("CREATE TABLE Recipe (pk INTEGER PRIMARY KEY, title TEXT, publisher TEXT, featuredImage TEXT, rating INTEGER, sourceUrl TEXT, ingredients TEXT)")
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}