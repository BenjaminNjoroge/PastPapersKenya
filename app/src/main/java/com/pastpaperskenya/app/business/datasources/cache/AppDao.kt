package com.pastpaperskenya.app.business.datasources.cache

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.model.download.Download
import com.pastpaperskenya.app.business.model.category.HomeCategory
import com.pastpaperskenya.app.business.model.category.SliderCategory
import com.pastpaperskenya.app.business.model.category.SubCategory
import com.pastpaperskenya.app.business.model.product.Product
import com.pastpaperskenya.app.business.model.product.ProductCategory
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

    @Query("SELECT * FROM product")
    fun getProducts(): LiveData<List<Product>>

    @Query("SELECT * FROM cart")
    fun getAllCartItems(): Flow<List<Cart>>

    @Query("SELECT * FROM cart WHERE productId= :productId")
    fun getCartProductById(productId: Int): Flow<Cart>

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertToCart(cart: Cart)

    @Delete
    suspend fun deleteAllCartItem(cart: Cart)

    @Query("DELETE FROM cart WHERE productId= :productId")
    suspend fun deleteCartItem(productId: Int)

    @Query("UPDATE cart SET productId= :productId, totalPrice= :totalPrice")
    fun updateCart(productId: Int, totalPrice: String)

    @Delete
    suspend fun deleteFromCart(cart: Cart)

}