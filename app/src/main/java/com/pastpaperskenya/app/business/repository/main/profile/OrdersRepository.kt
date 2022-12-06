package com.pastpaperskenya.app.business.repository.main.profile

import com.pastpaperskenya.app.business.model.orders.CreateOrder
import retrofit2.Response

interface OrdersRepository {
    

    suspend fun createOrder(order: CreateOrder): Response<CreateOrder>
}