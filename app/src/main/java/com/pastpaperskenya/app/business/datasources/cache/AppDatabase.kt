package com.pastpaperskenya.app.business.datasources.cache

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

}