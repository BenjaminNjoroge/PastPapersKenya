package com.pastpaperskenya.app.business.datasources.remote.services.payment

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.pastpaperskenya.app.business.model.lipanampesa.DatabaseKeys
import com.pastpaperskenya.app.business.model.lipanampesa.Payment
import com.pastpaperskenya.app.business.util.Constants
import kotlinx.coroutines.tasks.await

class PaymentServiceImpl (private val firestore: FirebaseFirestore) : PaymentService {

    override suspend fun getPayments(): List<Payment> {
        val snapshot= firestore.collection(Constants.FIREBASE_DATABASE_COLLECTION_PAYMENTS)
            .get().await()

        return snapshot.mapNotNull { queryDocumentSnapshot ->
            queryDocumentSnapshot.maoToPayment()
        }
    }

    private fun QueryDocumentSnapshot.maoToPayment() = Payment(
        this[DatabaseKeys.Payment.checkoutRequestId] as String,
        this[DatabaseKeys.Payment.customerId] as String,
        this[DatabaseKeys.Payment.date] as Long,
        this[DatabaseKeys.Payment.merchantRequestId] as String,
        this[DatabaseKeys.Payment.orderId] as String,
        this[DatabaseKeys.Payment.resultDesc] as String,
        this[DatabaseKeys.Payment.status] as String,
        this[DatabaseKeys.Payment.mpesaReceiptNumber] as String,
        (this[DatabaseKeys.Payment.phoneNumber] as Long?).toString()
    )
}