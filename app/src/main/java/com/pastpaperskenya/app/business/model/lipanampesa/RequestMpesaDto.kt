package com.pastpaperskenya.app.business.model.lipanampesa

import com.google.gson.annotations.SerializedName

data class RequestMpesaDto(
    @SerializedName("amount")
    val amount: String,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("customerId")
    val customerId: String,

    @SerializedName("orderId")
    val orderId: String
)
