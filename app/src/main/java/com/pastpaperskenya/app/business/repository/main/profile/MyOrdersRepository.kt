package com.pastpaperskenya.app.business.repository.main.profile

import com.pastpaperskenya.app.business.datasources.cache.AppDatabase
import com.pastpaperskenya.app.business.datasources.remote.RemoteDataSource
import com.pastpaperskenya.app.business.datasources.remote.services.auth.UserService
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
    private val retrofitService: RetrofitService,
    private val userService: UserService
) {

    suspend fun getMyOrders(customerId: Int?) = flow{
        emit(Resource.loading())

        val ordersResponse= retrofitService.getMyOrders(customerId)
        emit(Resource.success(ordersResponse))
    }.catch { e->
        emit(Resource.error(e.message ?: "Unkwown error"))
    }.flowOn(Dispatchers.IO)

    suspend fun getUserDetails(userId: String) : UserDetails?{
        return userService.getUserDetails(userId)
    }
}