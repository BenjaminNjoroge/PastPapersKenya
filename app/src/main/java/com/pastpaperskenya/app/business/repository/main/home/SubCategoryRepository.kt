package com.pastpaperskenya.app.business.repository.main.home

import com.pastpaperskenya.app.business.datasources.cache.AppDatabase
import com.pastpaperskenya.app.business.datasources.remote.RemoteDataSource
import com.pastpaperskenya.app.business.util.networkBoundResource
import javax.inject.Inject

class SubCategoryRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val appDatabase: AppDatabase
) {

    private val categoryDao= appDatabase.categoryDao()

    fun getSubCategory(parent:Int, perpage:Int)= networkBoundResource(
        databaseQuery = {categoryDao.getSubCategory(parent)},
        networkFetch = {remoteDataSource.getRemoteSubCategory(parent, perpage)},
        saveNetworkData = {categoryDao.insertSubCategory(it)}
    )

}