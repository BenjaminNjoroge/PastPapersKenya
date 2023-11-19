package com.pastpaperskenya.papers.business.datasources.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pastpaperskenya.papers.business.model.user.UserDetails
import com.pastpaperskenya.papers.business.model.cart.Cart
import com.pastpaperskenya.papers.business.model.download.Download
import com.pastpaperskenya.papers.business.model.category.HomeCategory
import com.pastpaperskenya.papers.business.model.category.SliderCategory
import com.pastpaperskenya.papers.business.model.category.SubCategory
import com.pastpaperskenya.papers.business.model.orders.Orders
import com.pastpaperskenya.papers.business.model.product.Product
import com.pastpaperskenya.papers.business.model.wishlist.WishList

@TypeConverters(Converters::class)
@Database(
    entities = [
        HomeCategory::class,
        SliderCategory::class,
        SubCategory::class,
        Download::class,
        Product::class,
        Cart::class,
        Orders::class,
        UserDetails::class,
        WishList::class
    ],
    version = 2, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

}