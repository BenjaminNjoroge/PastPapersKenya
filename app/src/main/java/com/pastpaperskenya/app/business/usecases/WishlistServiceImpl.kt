package com.pastpaperskenya.app.business.usecases

import com.pastpaperskenya.app.business.datasources.cache.AppDatabase
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.model.wishlist.WishList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WishlistServiceImpl @Inject constructor(
    private val database: AppDatabase
): WishlistService{

    private val appdao= database.appDao()

    override fun getWishlistItems(): Flow<List<WishList>> {
        return appdao.getAllWishlistItems()
    }

    override suspend fun insertWishlistItems(wishlist: WishList) {
        appdao.insertToWishlist(wishlist)
    }

    override suspend fun deleteWishlistItems(productId: Int) {
        appdao.deleteWishlistItem(productId)
    }
}