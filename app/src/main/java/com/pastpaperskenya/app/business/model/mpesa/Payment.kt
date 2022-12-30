package com.pastpaperskenya.app.business.model.mpesa

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
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
) : Parcelable{

    companion object {
        fun DocumentSnapshot.toPayments(): Payment? {
            return try {
                val checkoutRequestId= getString("checkoutRequestId")
                val customerId= getString("customerId")
                val date= getLong("date")
                val merchantRequestId= getString("merchantRequestId")
                val orderId= getString("orderId")
                val resultDesc= getString("resultDesc")
                val status= getString("status")
                val mpesaReceiptNumber= getString("mpesaReceiptNumber")
                val phoneNumber= getString("phoneNumber")
                val customerFirebaseId= getString("customerFirebaseId")
                Payment(checkoutRequestId, customerId, date, merchantRequestId, orderId, resultDesc, status, mpesaReceiptNumber, phoneNumber, customerFirebaseId)

            } catch (e: Exception){
                null
            }
        }
    }
}
