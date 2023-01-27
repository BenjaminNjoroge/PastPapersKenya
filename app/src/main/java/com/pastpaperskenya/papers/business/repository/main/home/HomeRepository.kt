package com.pastpaperskenya.papers.business.repository.main.home

import com.pastpaperskenya.papers.business.datasources.cache.AppDatabase
import com.pastpaperskenya.papers.business.datasources.remote.RemoteDataSource
import com.pastpaperskenya.papers.business.util.networkBoundResource
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val remoteDatasource: RemoteDataSource,
    private val appDatabase: AppDatabase
) {

    private val categoryDao= appDatabase.appDao()

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