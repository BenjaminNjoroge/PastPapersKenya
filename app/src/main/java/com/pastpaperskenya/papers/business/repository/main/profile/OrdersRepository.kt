package com.pastpaperskenya.papers.business.repository.main.profile

import com.pastpaperskenya.papers.business.model.orders.CreateOrder
import retrofit2.Response

interface OrdersRepository {
    

    suspend fun createOrder(order: CreateOrder): Response<CreateOrder>
}