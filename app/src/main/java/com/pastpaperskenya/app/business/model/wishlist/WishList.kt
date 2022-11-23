package com.pastpaperskenya.app.business.model.wishlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class WishList(

    @ColumnInfo(name="product_id") var productId: Int?,
    @ColumnInfo(name = "product_name") var productName: String?,
    @ColumnInfo(name = "product_regular_price") var productRegularPrice: String?,
    @ColumnInfo(name = "product_sale_price") var productSalePrice: String?,
    @ColumnInfo(name = "product_image") var productImage: String?,
    @ColumnInfo(name = "column_id") var categoryIds: Int?,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "discount") var discount: String?,
    @ColumnInfo(name = "review_count") var reviewCount: Int?,
    @ColumnInfo(name = "rating_text") var ratingText: String?
){
    @PrimaryKey(autoGenerate = true) var id= 0
}