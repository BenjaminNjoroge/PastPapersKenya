package com.pastpaperskenya.papers.business.usecases

import com.pastpaperskenya.papers.business.model.cart.Cart
import kotlinx.coroutines.flow.Flow

interface CartService{

    fun getCartItems(): Flow<List<Cart>>

    suspend fun insertCartItems(cart: Cart)

    suspend fun deleteCartItems(productId: Int)

    fun getProductCount(): Flow<Int?>

    suspend fun deleteAllCart()
}