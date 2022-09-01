package com.pastpaperskenya.app.business.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pastpaperskenya.app.business.model.Category

@TypeConverters(Converters::class)
@Database(entities = [Category::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun categoryDao(): CategoryDao

    companion object{
        @Volatile
        private var instance: AppDatabase? =null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this){ instance ?: buildDatabase(context).also { instance = it }}

        private fun buildDatabase(appContext: Context) = Room.databaseBuilder(
            appContext, AppDatabase::class.java, "pastpapers_db")
            .fallbackToDestructiveMigration()
            .build()
    }
}