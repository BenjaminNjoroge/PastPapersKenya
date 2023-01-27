package com.pastpaperskenya.papers.business.model.mpesa

import com.google.gson.annotations.SerializedName

class MpesaTokenResponse {
    @SerializedName("status")
    var status: String? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("code")
    var code: String? = null

    @SerializedName("data")
    var mpesaTokenData: MpesaTokenData? = null

    inner class MpesaTokenData {
        @SerializedName("access_token")
        var token: String? = null

        @SerializedName("expires_in")
        var expires_in: String? = null
    }
}