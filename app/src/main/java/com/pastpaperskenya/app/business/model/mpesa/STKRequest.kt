package com.pastpaperskenya.app.business.model.mpesa

import com.google.gson.annotations.SerializedName

class STKRequest(
    @field:SerializedName("total_amount") var total_amount: String,
    @field:SerializedName("phone_number") var phone_number: String,
    @field:SerializedName("order_id") var order_id: String,
    @field:SerializedName("accesstoken") var accesstoken: String
)