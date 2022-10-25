package com.pastpaperskenya.app.business.repository.main.profile

import com.pastpaperskenya.app.business.datasources.cache.AppDatabase
import com.pastpaperskenya.app.business.datasources.remote.RemoteDataSource
import com.pastpaperskenya.app.business.datasources.remote.services.auth.UserService
import com.pastpaperskenya.app.business.model.auth.UserDetails
import com.pastpaperskenya.app.business.util.networkBoundResource
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