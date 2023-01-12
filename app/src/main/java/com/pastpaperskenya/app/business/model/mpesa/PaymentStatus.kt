package com.pastpaperskenya.app.business.model.mpesa

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentStatus(
    val orderId: String,
    val paymentStatus: String
): Parcelable{

    companion object {
        fun DocumentSnapshot.toPaymentStatus() : PaymentStatus?{
            return try {
                val orderId= getString("orderId")
                val paymentStatus= getString("paymentStatus")
                PaymentStatus(orderId.toString(), paymentStatus.toString())
            } catch (e: Exception){
                null
            }
        }

    }
}
