package com.pastpaperskenya.app.business.repository.main.profile

import com.pastpaperskenya.app.business.datasources.cache.AppDatabase
import com.pastpaperskenya.app.business.datasources.remote.RemoteDataSource
import com.pastpaperskenya.app.business.usecases.FirestoreUserService
import com.pastpaperskenya.app.business.datasources.remote.services.main.RetrofitService
import com.pastpaperskenya.app.business.model.auth.UserDetails
import com.pastpaperskenya.app.business.util.networkBoundResource
import com.pastpaperskenya.app.business.util.sealed.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MyOrdersRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val database: AppDatabase
) {

    private val appdao= database.appDao()

     fun getMyOrders(customerId: Int) = networkBoundResource(
        databaseQuery = {appdao.getMyOrders(customerId)},
        networkFetch = {remoteDataSource.getMyOrders(customerId)},
        saveNetworkData = {appdao.insertMyOrders(it)}
    )


}