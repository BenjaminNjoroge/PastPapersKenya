package com.pastpaperskenya.papers.business.repository.main.cart

import com.pastpaperskenya.papers.business.model.cart.Cart
import com.pastpaperskenya.papers.business.usecases.CartService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartService: CartService) {


    fun getCartItems():Flow<List<Cart>> {
        return cartService.getCartItems()
    }

    suspend fun deleteItems(productId: Int){
        cartService.deleteCartItems(productId)
    }

    fun getProductCount(): Flow<Int?>{
        return cartService.getProductCount()
    }

    suspend fun deleteAllCart(){
        cartService.deleteAllCart()
    }

}