package com.pastpaperskenya.app.business.repository.main.home

import com.pastpaperskenya.app.business.datasources.cache.AppDatabase
import com.pastpaperskenya.app.business.datasources.remote.RemoteDataSource
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.model.product.ProductCategory
import com.pastpaperskenya.app.business.util.networkBoundResource
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    private val database: AppDatabase,
    private val remoteDataSource: RemoteDataSource,
) {
    private val appDao= database.appDao()

    fun getProducts(perpage: Int, category:Int)= networkBoundResource(
        databaseQuery = {appDao.getProducts()},
        networkFetch = {remoteDataSource.getProducts(perpage, category)},
        saveNetworkData = {appDao.insertProducts(it)}
    )

    suspend fun addProductToCart(cart: Cart){
        appDao.insertToCart(cart)
    }

    suspend fun removeProductFromCart(cart: Cart){
        appDao.deleteFromCart(cart)
    }

}