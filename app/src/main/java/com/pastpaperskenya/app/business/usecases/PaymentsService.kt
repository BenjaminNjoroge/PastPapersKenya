package com.pastpaperskenya.app.business.usecases

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.pastpaperskenya.app.business.model.mpesa.Payment
import com.pastpaperskenya.app.business.model.mpesa.PaymentStatus
import com.pastpaperskenya.app.business.model.mpesa.PaymentStatus.Companion.toPaymentStatus
import com.pastpaperskenya.app.business.util.Constants
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PaymentsService {

    suspend fun getPaymentData(orderId: String, email: String): Flow<PaymentStatus> {
        val db= FirebaseFirestore.getInstance()
        
        return callbackFlow { 
            val listenerRegistration=
                db.collection(Constants.FIREBASE_DATABASE_COLLECTION_ORDER)
                .document(email)
                    .collection("orderId")
                    .document(orderId)
                    .addSnapshotListener { document: DocumentSnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                    if (firebaseFirestoreException !=null){
                        cancel(message = "Error fetching payments")
                        return@addSnapshotListener
                    }
                    val map= document?.toPaymentStatus()
                    trySend(map!!).isSuccess
                }
            awaitClose {
                listenerRegistration.remove()
            }
        }
    }

    companion object {
        private const val TAG = "PaymentsService"
    }
}