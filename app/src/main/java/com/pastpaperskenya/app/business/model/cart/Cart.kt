package com.pastpaperskenya.app.business.model.cart

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class Cart(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val productId: Int?,
    val productName: String?,
    val productPrice: String?,
    val totalPrice: String?,
    val productImage: String?,
    val categoryIds: Int
)
