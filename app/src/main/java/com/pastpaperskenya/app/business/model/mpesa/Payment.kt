package com.pastpaperskenya.app.business.model.mpesa

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class Payment(
    val checkoutRequestId: String?=null,
    val customerId: String?=null,
    val date: Long?=null,
    val merchantRequestId: String?=null,
    val orderId: String?=null,
    val resultDesc: String?=null,
    val status: String?=null,
    val mpesaReceiptNumber: String?=null,
    val phoneNumber: String?=null,
    val customerFirebaseId: String?=null,
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
