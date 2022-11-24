package com.pastpaperskenya.app.business.datasources.cache

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pastpaperskenya.app.business.model.user.UserDetails
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.model.download.Download
import com.pastpaperskenya.app.business.model.category.HomeCategory
import com.pastpaperskenya.app.business.model.category.SliderCategory
import com.pastpaperskenya.app.business.model.category.SubCategory
import com.pastpaperskenya.app.business.model.orders.Orders
import com.pastpaperskenya.app.business.model.product.Product
import com.pastpaperskenya.app.business.model.wishlist.WishList

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
    version = 1, exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

}