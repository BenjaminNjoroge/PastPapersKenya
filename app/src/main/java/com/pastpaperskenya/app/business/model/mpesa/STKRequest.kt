package com.pastpaperskenya.app.business.model.mpesa

import com.google.gson.annotations.SerializedName

class STKRequest(

    @SerializedName("total_amount")
    val total_amount: String,

    @SerializedName("phone_number")
    val phone_number: String,

    @SerializedName("order_id")
    val order_id: String,

    @SerializedName("accesstoken")
    val accesstoken: String
)