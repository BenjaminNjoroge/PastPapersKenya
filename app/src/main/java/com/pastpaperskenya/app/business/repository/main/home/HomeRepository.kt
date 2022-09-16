package com.pastpaperskenya.app.business.repository.main.home

import androidx.room.withTransaction
import com.pastpaperskenya.app.business.datasources.cache.AppDatabase
import com.pastpaperskenya.app.business.datasources.remote.CategoryRemoteDataSource
import com.pastpaperskenya.app.business.services.main.RetrofitService
import com.pastpaperskenya.app.business.util.networkBoundResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val remoteDatasource: CategoryRemoteDataSource,
    private val appDatabase: AppDatabase
) {

    private val categoryDao= appDatabase.categoryDao()

    fun getParentCategory(parent: Int, filter: ArrayList<Int>)= networkBoundResource(
        databaseQuery = {categoryDao.getParentCategory()},
        networkFetch = {remoteDatasource.getRemoteParentCategory(parent, filter)},
        saveNetworkData = {categoryDao.insertParentCategory(it)}
    )

    fun getSliderCategory(parent: Int)= networkBoundResource(
        databaseQuery = {categoryDao.getSliderCategory()},
        networkFetch = {remoteDatasource.getRemoteSliderCategory(parent)},
        saveNetworkData = {categoryDao.insertSliderCategory(it)}
    )

}