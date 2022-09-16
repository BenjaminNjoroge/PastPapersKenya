package com.pastpaperskenya.app.business.datasources.cache

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pastpaperskenya.app.business.model.category.HomeCategory
import com.pastpaperskenya.app.business.model.category.SliderCategory

@Dao
interface CategoryDao {

    @Query("SELECT * FROM slidercategory")
    fun getSliderCategory(): LiveData<List<SliderCategory>>

    @Query("SELECT * FROM homecategory")
    fun getParentCategory() : LiveData<List<HomeCategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParentCategory(homeCategory: List<HomeCategory>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSliderCategory(sliderCategory: List<SliderCategory>)


}