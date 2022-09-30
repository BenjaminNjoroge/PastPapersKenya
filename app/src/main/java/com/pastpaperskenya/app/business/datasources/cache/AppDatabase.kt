package com.pastpaperskenya.app.business.datasources.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pastpaperskenya.app.business.model.download.Download
import com.pastpaperskenya.app.business.model.category.HomeCategory
import com.pastpaperskenya.app.business.model.category.SliderCategory
import com.pastpaperskenya.app.business.model.category.SubCategory
import com.pastpaperskenya.app.business.model.product.Product

@TypeConverters(Converters::class)
@Database(entities = [
    HomeCategory::class,
    SliderCategory::class,
    SubCategory::class,
    Download::class,
    Product::class
                     ],
    version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun appDao(): AppDao

}