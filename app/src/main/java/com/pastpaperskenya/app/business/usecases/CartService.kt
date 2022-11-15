package com.pastpaperskenya.app.business.usecases

import androidx.lifecycle.LiveData
import com.pastpaperskenya.app.business.datasources.cache.AppDatabase
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.util.sealed.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CartService{

    fun getCartItems(): Flow<List<Cart>>

    suspend fun insertCartItems(cart: Cart)

    suspend fun deleteCartItems(productId: Int)
}