package com.pastpaperskenya.papers.business.repository.main.home

import com.pastpaperskenya.papers.business.datasources.cache.AppDatabase
import com.pastpaperskenya.papers.business.datasources.remote.RemoteDataSource
import com.pastpaperskenya.papers.business.util.networkBoundResource
import javax.inject.Inject

class ProductDetailRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val database: AppDatabase
) {

    private val detailsDao= database.appDao()

    fun getProductsDetail(id: Int)= networkBoundResource(
        databaseQuery = {detailsDao.getProductDetail(id)},
        networkFetch = {remoteDataSource.getProductDetail(id)},
        saveNetworkData = {
            detailsDao.insertProductDetail(it)}
    )
}