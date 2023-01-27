package com.pastpaperskenya.papers.business.repository.main.profile

import com.pastpaperskenya.papers.business.datasources.cache.AppDatabase
import com.pastpaperskenya.papers.business.datasources.remote.RemoteDataSource
import com.pastpaperskenya.papers.business.util.networkBoundResource
import javax.inject.Inject

class MyOrdersDetailRepository @Inject constructor
    (
    private val database: AppDatabase,
    private val remoteDataSource: RemoteDataSource,
) {
        private val appDao= database.appDao()

        fun getMyOrdersDetails(id: Int)= networkBoundResource(
            databaseQuery = {appDao.getMyOrdersDetails()},
            networkFetch = {remoteDataSource.getMyOrderDetails(id)},
            saveNetworkData = {appDao.insertMyOrderDetails(it)}
        )

}