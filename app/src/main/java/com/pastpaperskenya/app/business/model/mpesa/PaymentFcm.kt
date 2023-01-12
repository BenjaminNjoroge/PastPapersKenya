package com.pastpaperskenya.app.business.model.mpesa

import com.google.gson.annotations.SerializedName

data class PaymentFcm (
//    val amount: Int?= null,
//    val checkoutRequestID: String?=null,
//    val customerFirebaseID: String?=null,
//    val customerId: String?=null,
//    val date: String?=null,
//    val merchantRequestID: String?=null,
//    val mpesaReceiptNumber: String?=null,
//    val orderId: String?=null,
//    val phoneNumber: Int?=null,
//    val resultCode: Int?=null,
//    val resultDesc: String?=null,
//    val status: String?=null,
//    val transactionDate: Int?=null

    @SerializedName("data")
    val fireData: FireData,

    @SerializedName("topicId")
    val fireTopicId: String
) {
    data class FireData (
        @SerializedName("title")
        val title: String,

        @SerializedName("body")
        val body: String
            )
}