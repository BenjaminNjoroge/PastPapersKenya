package com.pastpaperskenya.app.business.repository.main.home

import com.pastpaperskenya.app.business.datasources.remote.services.main.RetrofitApiService
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.model.product.Product
import com.pastpaperskenya.app.business.usecases.CartService
import com.pastpaperskenya.app.business.util.sealed.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    private val cartService: CartService,
    private val retrofitApiService: RetrofitApiService,
) {

    fun getProducts(perpage: Int, category:String):Flow<Resource<List<Product>>>{
        return flow {
            emit(Resource.loading())
            val response= retrofitApiService.getProductsList(perpage, category)
            emit(Resource.success(response))
        }.catch { e->
            emit(Resource.error(e.message ?: "Unkwown error"))
        }.flowOn(Dispatchers.IO)
    }


    suspend fun insertCartItems(cart: Cart){
        cartService.insertCartItems(cart)
    }

    suspend fun deleteCartItems(productId: Int){
        cartService.deleteCartItems(productId)
    }
    
}