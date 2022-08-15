package com.pastpaperskenya.app.business.services.order

import com.pastpaperskenya.app.business.model.lipanampesa.DatabaseKeys
import com.pastpaperskenya.app.business.model.lipanampesa.Order
import com.pastpaperskenya.app.business.model.lipanampesa.OrderItem

interface OrderService {

    suspend fun placeNewOrder(order: DatabaseKeys.Order, orderItem: List<OrderItem>)

    suspend fun getOrderItems(orderId: String): List<OrderItem>

//    suspend fun getOrder(orderId: String): Order
//
//    suspend fun getAllOrder(): List<Order>
}