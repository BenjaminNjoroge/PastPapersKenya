package com.pastpaperskenya.app.business.repository.main.cart

import com.pastpaperskenya.app.business.datasources.cache.AppDatabase
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.usecases.CartService
import com.pastpaperskenya.app.business.util.sealed.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartService: CartService) {


    fun getCartItems():Flow<List<Cart>> {
        return cartService.getCartItems()
    }

    suspend fun deleteItems(productId: Int){
        cartService.deleteCartItems(productId)
    }


}