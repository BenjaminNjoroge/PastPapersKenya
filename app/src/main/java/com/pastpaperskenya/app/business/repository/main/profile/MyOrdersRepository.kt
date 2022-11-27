package com.pastpaperskenya.app.business.repository.main.profile

import com.pastpaperskenya.app.business.datasources.cache.AppDatabase
import com.pastpaperskenya.app.business.datasources.remote.BaseDataSource
import com.pastpaperskenya.app.business.datasources.remote.RemoteDataSource
import com.pastpaperskenya.app.business.datasources.remote.services.main.RetrofitApiService
import com.pastpaperskenya.app.business.model.orders.CreateOrder
import com.pastpaperskenya.app.business.util.networkBoundResource
import com.pastpaperskenya.app.business.util.sealed.Resource
import retrofit2.Response
import javax.inject.Inject

class MyOrdersRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val database: AppDatabase,
    private val retrofitApiService: RetrofitApiService
) : BaseDataSource() {

    private val appdao= database.appDao()

     fun getMyOrders(customerId: Int) = networkBoundResource(
        databaseQuery = {appdao.getMyOrders(customerId)},
        networkFetch = {remoteDataSource.getMyOrders(customerId)},
        saveNetworkData = {appdao.insertMyOrders(it)}
    )

    suspend fun createOrder(order: CreateOrder): Resource<CreateOrder>{
        return safeApiCall { retrofitApiService.createOrder(order) }
    }
}