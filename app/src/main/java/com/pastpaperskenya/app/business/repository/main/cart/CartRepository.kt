package com.pastpaperskenya.app.business.repository.main.cart

import com.pastpaperskenya.app.business.datasources.cache.AppDatabase
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.util.sealed.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepository @Inject constructor(private val database: AppDatabase) {

    private val appDao= database.appDao()

    fun getCartItems():Flow<List<Cart>> {
        return appDao.getAllCartItems()
    }

    suspend fun deleteItems(cart: Cart){
        appDao.deleteFromCart(cart)
    }
}