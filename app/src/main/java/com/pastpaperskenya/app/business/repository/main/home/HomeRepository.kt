package com.pastpaperskenya.app.business.repository.main.home

import com.pastpaperskenya.app.business.cache.CategoryDao
import com.pastpaperskenya.app.business.services.main.CategoryRemoteDataSource
import com.pastpaperskenya.app.business.services.main.CategoryService
import com.pastpaperskenya.app.business.util.performDataAccessStrategy
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val categoryRemoteDataSource: CategoryRemoteDataSource,
    private val localDataSource: CategoryDao
) {
    fun getCategories() = performDataAccessStrategy(
        databaseQuery = {localDataSource.getCategories()},
        networkCall = {categoryRemoteDataSource.getCategories()},
        saveCallRequest = {localDataSource.insertAll((it))}
    )

}