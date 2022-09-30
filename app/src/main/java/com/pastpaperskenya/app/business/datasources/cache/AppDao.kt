package com.pastpaperskenya.app.business.datasources.cache

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pastpaperskenya.app.business.model.download.Download
import com.pastpaperskenya.app.business.model.category.HomeCategory
import com.pastpaperskenya.app.business.model.category.SliderCategory
import com.pastpaperskenya.app.business.model.category.SubCategory
import com.pastpaperskenya.app.business.model.product.Product

@Dao
interface AppDao {

    @Query("SELECT * FROM slidercategory")
    fun getSliderCategory(): LiveData<List<SliderCategory>>

    @Query("SELECT * FROM homecategory")
    fun getParentCategory() : LiveData<List<HomeCategory>>

    @Query("SELECT * FROM subcategory WHERE parent= :parent ORDER BY name ASC")
    fun getSubCategory(parent:Int) : LiveData<List<SubCategory>>

    @Query("SELECT * FROM downloads")
    fun getDownloads(): LiveData<List<Download>>

    @Query("SELECT * FROM product WHERE id=:id")
    fun getProductDetail(id: Int): LiveData<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParentCategory(homeCategory: List<HomeCategory>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSliderCategory(sliderCategory: List<SliderCategory>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubCategory(sliderCategory: List<SubCategory>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDownloads(downloads: List<Download>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductDetail(product: Product)


}