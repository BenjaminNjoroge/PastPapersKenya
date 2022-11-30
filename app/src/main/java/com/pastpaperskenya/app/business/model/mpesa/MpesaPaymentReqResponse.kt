package com.pastpaperskenya.app.business.model.mpesa

import com.google.gson.annotations.SerializedName
import com.pastpaperskenya.app.business.model.mpesa.MpesaPaymentReqResponse.MpesaPaymentReqResponseData

class MpesaPaymentReqResponse {

    @SerializedName("status")
    var status: String? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("code")
    var code: String? = null

    @SerializedName("data")
    var mpesaPaymentReqResponseData: MpesaPaymentReqResponseData? = null

    inner class MpesaPaymentReqResponseData {
        @SerializedName("MerchantRequestID")
        var merchantRequestID: String? = null

        @SerializedName("CheckoutRequestID")
        var checkoutRequestID: String? = null

        @SerializedName("ResponseCode")
        var responseCode: String? = null

        @SerializedName("ResponseDescription")
        var responseDescription: String? = null

        @SerializedName("CustomerMessage")
        var customerMessage: String? = null
    }
}