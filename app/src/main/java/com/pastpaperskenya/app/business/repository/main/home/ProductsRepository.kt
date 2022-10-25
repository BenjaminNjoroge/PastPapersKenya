package com.pastpaperskenya.app.business.repository.main.home

import com.pastpaperskenya.app.business.datasources.cache.AppDatabase
import com.pastpaperskenya.app.business.datasources.remote.RemoteDataSource
import com.pastpaperskenya.app.business.datasources.remote.services.main.RetrofitService
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.model.product.Product
import com.pastpaperskenya.app.business.util.networkBoundResource
import com.pastpaperskenya.app.business.util.sealed.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    private val database: AppDatabase,
    private val retrofitService: RetrofitService,
) {
    private val appDao= database.appDao()

    fun getProducts(perpage: Int, category:Int):Flow<Resource<List<Product>>>{
        return flow {
            emit(Resource.loading())
            val response= retrofitService.getProductsList(perpage, category)
            emit(Resource.success(response))
        }.catch { e->
            emit(Resource.error(e.message ?: "Unkwown error"))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addProductToCart(cart: Cart){
        appDao.insertToCart(cart)
    }

    suspend fun removeProductFromCart(cart: Cart){
        appDao.deleteFromCart(cart)
    }

}