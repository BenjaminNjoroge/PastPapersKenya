package com.pastpaperskenya.papers.business.model.cart

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class Cart(

    @ColumnInfo(name="product_id") var productId: Int,
    @ColumnInfo(name = "product_name") var productName: String?,
    @ColumnInfo(name = "product_price") var productPrice: String?,
    @ColumnInfo(name = "total_price") var totalPrice: String?,
    @ColumnInfo(name = "product_image") var productImage: String?,
    @ColumnInfo(name = "column_id") var categoryIds: Int
){
    @PrimaryKey(autoGenerate = true) var id= 0
}
