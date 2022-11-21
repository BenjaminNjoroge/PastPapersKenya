package com.pastpaperskenya.app.business.repository.main.wishlist

import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.model.wishlist.WishList
import com.pastpaperskenya.app.business.usecases.CartService
import com.pastpaperskenya.app.business.usecases.WishlistService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WishlistRepository @Inject constructor(
    private val wishlistService: WishlistService
) {


    fun getWishlistItems(): Flow<List<WishList>> {
        return wishlistService.getWishlistItems()
    }

    suspend fun deleteItems(productId: Int){
        wishlistService.deleteWishlistItems(productId)
    }

    suspend fun addItemsToWishlist(wishlist: WishList){
        wishlistService.insertWishlistItems(wishlist)
    }
}