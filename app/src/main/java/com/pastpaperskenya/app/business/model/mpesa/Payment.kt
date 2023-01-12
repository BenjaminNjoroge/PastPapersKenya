package com.pastpaperskenya.app.business.model.mpesa

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

data class Payment(
    val CheckoutRequestID: String?=null,
    val CustomerId: String?=null,
    val Date: Long?=null,
    val MerchantRequestID: String?=null,
    val OrderId: String?=null,
    val ResultDesc: String?=null,
    val Status: String?=null,
    val MpesaReceiptNumber: String?=null,
    val PhoneNumber: String?=null,
    val CustomerFirebaseID: String?=null,
    val email: String?=null
)

