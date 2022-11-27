package com.pastpaperskenya.app.business.repository.main.profile

import androidx.lifecycle.LiveData
import com.pastpaperskenya.app.business.model.orders.CreateOrder
import com.pastpaperskenya.app.business.util.sealed.Resource
import retrofit2.Response

interface OrdersRepository {
    

    suspend fun createOrder(order: CreateOrder): Response<CreateOrder>
}