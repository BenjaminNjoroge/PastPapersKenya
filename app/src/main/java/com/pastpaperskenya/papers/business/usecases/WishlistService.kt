package com.pastpaperskenya.papers.business.usecases

import com.pastpaperskenya.papers.business.model.wishlist.WishList
import kotlinx.coroutines.flow.Flow

interface WishlistService {

    fun getWishlistItems(): Flow<List<WishList>>

    suspend fun insertWishlistItems(wishlist: WishList)

    suspend fun deleteWishlistItems(productId: Int)
}