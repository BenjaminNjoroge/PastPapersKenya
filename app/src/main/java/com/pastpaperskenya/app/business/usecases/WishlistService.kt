package com.pastpaperskenya.app.business.usecases

import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.model.wishlist.WishList
import kotlinx.coroutines.flow.Flow

interface WishlistService {

    fun getWishlistItems(): Flow<List<WishList>>

    suspend fun insertWishlistItems(wishlist: WishList)

    suspend fun deleteWishlistItems(productId: Int)
}