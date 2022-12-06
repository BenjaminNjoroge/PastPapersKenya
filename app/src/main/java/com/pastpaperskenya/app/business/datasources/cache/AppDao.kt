package com.pastpaperskenya.app.business.datasources.cache

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pastpaperskenya.app.business.model.user.UserDetails
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.model.download.Download
import com.pastpaperskenya.app.business.model.category.HomeCategory
import com.pastpaperskenya.app.business.model.category.SliderCategory
import com.pastpaperskenya.app.business.model.category.SubCategory
import com.pastpaperskenya.app.business.model.orders.Orders
import com.pastpaperskenya.app.business.model.product.Product
import com.pastpaperskenya.app.business.model.wishlist.WishList
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    @Query("SELECT * FROM slidercategory")
    fun getSliderCategory(): LiveData<List<SliderCategory>>

    @Query("SELECT * FROM homecategory")
    fun getParentCategory() : LiveData<List<HomeCategory>>

    @Query("SELECT * FROM subcategory WHERE parent= :parent ORDER BY name ASC")
    fun getSubCategory(parent:Int) : LiveData<List<SubCategory>>

    @Query("SELECT * FROM downloads")
    fun getDownloads(): LiveData<List<Download>>

    @Query("SELECT * FROM product WHERE id=:id")
    fun getProductDetail(id: Int): LiveData<Product>

    @Query("SELECT * FROM cart ORDER BY id DESC")
    fun getAllCartItems(): Flow<List<Cart>>

    @Query("SELECT * FROM wishlist ORDER BY id DESC")
    fun getAllWishlistItems(): Flow<List<WishList>>

    @Query("SELECT * FROM cart WHERE product_id= :productId")
    fun getCartProductById(productId: Int): Flow<Cart>

    @Query("SELECT * FROM orders")
    fun getMyOrdersDetails(): LiveData<Orders>

    @Query("SELECT * FROM orders WHERE customer_id= :customerId ORDER BY date_created DESC")
    fun getMyOrders(customerId: Int): LiveData<List<Orders>>

    @Query("SELECT * FROM users WHERE userServerId=:userServerId")
    fun getUserDetails(userServerId: Int): Flow<UserDetails>

    @Query("SELECT SUM(total_price) FROM cart")
    fun getPriceCount(): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParentCategory(homeCategory: List<HomeCategory>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSliderCategory(sliderCategory: List<SliderCategory>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubCategory(sliderCategory: List<SubCategory>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDownloads(downloads: List<Download>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductDetail(product: Product)

    @Insert(onConflict = OnConflictStrategy.IGNORE )
    suspend fun insertToCart(cart: Cart)

    @Insert(onConflict = OnConflictStrategy.IGNORE )
    suspend fun insertToWishlist(wishList: WishList)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserDetails(userDetails: UserDetails):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMyOrders(orders: List<Orders>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMyOrderDetails(orders: Orders)

    @Query("DELETE FROM cart WHERE product_id= :productId")
    suspend fun deleteCartItem(productId: Int)

    @Query("DELETE FROM wishlist WHERE product_id= :productId")
    suspend fun deleteWishlistItem(productId: Int)

    @Query("UPDATE users SET phone=:phone, firstname=:firstname, lastname=:lastname, country=:country, county=:county, image= :photo WHERE userServerId= :userServerId")
    suspend fun updateUserDetails(phone: String, firstname: String, lastname: String, country: String, county: String, userServerId: Int, photo:String?)

    @Query("DELETE FROM cart")
    suspend fun deleteAllFromCart()

}