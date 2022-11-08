package com.pastpaperskenya.app.business.model.mpesa

import com.google.gson.annotations.SerializedName

data class MpesaPaymentReqResponseData(

    val merchantRequestID: String,
    val checkoutRequestID: String,
    val responseCode: String,
    val responseDescription: String,
    val customerMessage: String
)
