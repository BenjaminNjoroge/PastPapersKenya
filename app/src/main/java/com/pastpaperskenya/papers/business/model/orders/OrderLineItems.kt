package com.pastpaperskenya.papers.business.model.orders

import com.google.gson.annotations.SerializedName

data class OrderLineItems (

    @SerializedName("quantity")
    val quantity: Int?,

    @SerializedName("product_id")
    val productId: Int?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("total")
    val total: String?,

    @SerializedName("price")
    val price: String?
        )