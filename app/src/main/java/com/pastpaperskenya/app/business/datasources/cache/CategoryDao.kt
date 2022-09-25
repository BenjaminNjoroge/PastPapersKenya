package com.pastpaperskenya.app.business.datasources.cache

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pastpaperskenya.app.business.model.category.HomeCategory
import com.pastpaperskenya.app.business.model.category.SliderCategory
import com.pastpaperskenya.app.business.model.category.SubCategory

@Dao
interface CategoryDao {

    @Query("SELECT * FROM slidercategory")
    fun getSliderCategory(): LiveData<List<SliderCategory>>

    @Query("SELECT * FROM homecategory")
    fun getParentCategory() : LiveData<List<HomeCategory>>

    @Query("SELECT * FROM subcategory WHERE parent= :parent")
    fun getSubCategory(parent:Int) : LiveData<List<SubCategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParentCategory(homeCategory: List<HomeCategory>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSliderCategory(sliderCategory: List<SliderCategory>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubCategory(sliderCategory: List<SubCategory>)


}