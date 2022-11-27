package com.pastpaperskenya.app.business.model.orders

import com.google.gson.annotations.SerializedName

data class OrderLineItems (

    @SerializedName("quantity")
    val quantity: Int?,

    @SerializedName("product_id")
    val productId: Int?,

    @SerializedName("total")
    val total: String?,

    @SerializedName("price")
    val price: String?
        )