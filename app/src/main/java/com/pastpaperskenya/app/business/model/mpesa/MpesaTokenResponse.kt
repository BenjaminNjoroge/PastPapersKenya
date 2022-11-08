package com.pastpaperskenya.app.business.model.mpesa

data class MpesaTokenResponse (
    val status: String,
    val message: String,
    val code: String,
    val data: MpesaTokenData
        )