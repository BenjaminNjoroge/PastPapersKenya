package com.pastpaperskenya.app.business.model.mpesa

data class StkPushRequest(
    val total_amount: String,
    val phone_number: String,
    val order_id: String,
    val accesstoken: String
)
