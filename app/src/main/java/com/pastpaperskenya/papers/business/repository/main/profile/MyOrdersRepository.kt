package com.pastpaperskenya.papers.business.repository.main.profile

import com.pastpaperskenya.papers.business.datasources.cache.AppDatabase
import com.pastpaperskenya.papers.business.datasources.remote.BaseDataSource
import com.pastpaperskenya.papers.business.datasources.remote.RemoteDataSource
import com.pastpaperskenya.papers.business.datasources.remote.services.main.RetrofitApiService
import com.pastpaperskenya.papers.business.model.orders.CreateOrder
import com.pastpaperskenya.papers.business.util.networkBoundResource
import com.pastpaperskenya.papers.business.util.sealed.NetworkResult
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

    suspend fun createOrder(order: CreateOrder): NetworkResult<CreateOrder>{
        return safeApiCall { retrofitApiService.createOrder(order) }
    }

    suspend fun updateOrder(id: Int, paid: Boolean, customerId: Int): Response<CreateOrder> =
        retrofitApiService.updateOrder(id, paid, customerId)

}