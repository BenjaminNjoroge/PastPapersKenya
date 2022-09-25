package com.pastpaperskenya.app.business.datasources.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pastpaperskenya.app.business.model.category.HomeCategory
import com.pastpaperskenya.app.business.model.category.SliderCategory
import com.pastpaperskenya.app.business.model.category.SubCategory

@TypeConverters(Converters::class)
@Database(entities = [
    HomeCategory::class,
    SliderCategory::class,
    SubCategory::class
                     ],
    version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun categoryDao(): CategoryDao

}