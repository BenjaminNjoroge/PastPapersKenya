package com.pastpaperskenya.app.business.model.mpesa

import com.google.gson.annotations.SerializedName


data class MpesaPaymentReqResponse (

        val status: String,
        val message: String,
        val code: String,
        val data: MpesaPaymentReqResponseData
)

