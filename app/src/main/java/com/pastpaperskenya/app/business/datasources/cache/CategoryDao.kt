package com.pastpaperskenya.app.business.datasources.cache

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pastpaperskenya.app.business.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    fun getCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM categories")
    fun getLocalCategory() : LiveData<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(category: List<Category>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()
}