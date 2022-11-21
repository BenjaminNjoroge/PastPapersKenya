package com.pastpaperskenya.app.business.model.mpesa

import com.google.gson.annotations.SerializedName
import com.pastpaperskenya.app.business.model.mpesa.CheckMpesaPaymentStatus.CheckMpesaPaymentStatusData

class CheckMpesaPaymentStatus {
    @SerializedName("status")
    var status: String? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("code")
    var code: String? = null

    @SerializedName("data")
    var checkMpesaPaymentStatusData: CheckMpesaPaymentStatusData? = null

    inner class CheckMpesaPaymentStatusData {
        @SerializedName("ResponseCode")
        var responseCode: String? = null

        @SerializedName("ResponseDescription")
        var responseDescription: String? = null

        @SerializedName("MerchantRequestID")
        var merchantRequestID: String? = null

        @SerializedName("CheckoutRequestID")
        var checkoutRequestID: String? = null

        @SerializedName("ResultCode")
        var resultCode: String? = null

        @SerializedName("ResultDesc")
        var resultDesc: String? = null

        @SerializedName("errorMessage")
        var errorMessage: String? = null
    }
}