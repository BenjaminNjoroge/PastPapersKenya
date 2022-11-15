package com.pastpaperskenya.app.business.usecases

import androidx.lifecycle.LiveData
import com.pastpaperskenya.app.business.datasources.cache.AppDatabase
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.util.sealed.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartServiceImpl @Inject constructor(
    private val database: AppDatabase
) : CartService {

    private val appdao= database.appDao()

    override fun getCartItems(): Flow<List<Cart>> {
        return appdao.getAllCartItems()
    }

    override suspend fun insertCartItems(cart: Cart) {
        return appdao.insertToCart(cart)
    }

    override suspend fun deleteCartItems(productId: Int) {
        appdao.deleteCartItem(productId)
    }
}