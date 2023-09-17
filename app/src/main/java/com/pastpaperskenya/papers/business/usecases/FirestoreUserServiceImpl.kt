package com.pastpaperskenya.papers.business.usecases

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pastpaperskenya.papers.business.model.mpesa.Payment
import com.pastpaperskenya.papers.business.model.user.UserDetails
import com.pastpaperskenya.papers.business.model.user.UserDetails.Companion.toUserDetails
import com.pastpaperskenya.papers.business.util.Constants
import kotlinx.coroutines.tasks.await


class FirestoreUserServiceImpl : FirestoreUserService {

    private val db: FirebaseFirestore= FirebaseFirestore.getInstance()

    override suspend fun saveUserDetails(userDetails: UserDetails) {
        userDetails.email?.let {
            Firebase.firestore.collection(Constants.FIREBASE_DATABASE_COLLECTION_USER).document(
                it
            ).set(userDetails).await()
        }
    }

    override suspend fun updateUserDetails(
        userId: String,
        phone: String?,
        firstname: String?,
        lastname: String?,
        country: String?,
        county: String?,
        email: String
    ) {
        val user= hashMapOf(
            "phone" to phone,
            "firstname" to firstname,
            "lastname" to lastname,
            "country" to country,
            "county" to county,
        )

        Firebase.firestore.collection(Constants.FIREBASE_DATABASE_COLLECTION_USER).document(email).update(
            user as Map<String, Any>
        )
    }

    override suspend fun getFirestoreUserDetails(email: String): UserDetails? {
           return Firebase.firestore.collection(Constants.FIREBASE_DATABASE_COLLECTION_USER)
                .document(email)
                .get().await().toUserDetails()

    }

    override suspend fun savePendingPaymentFirebase(paymentDetails: Payment) {
         Firebase.firestore.collection(Constants.FIREBASE_DATABASE_COLLECTION_PAYMENTS)
            .document(paymentDetails.CheckoutRequestID.toString())
             .set(paymentDetails).await()
    }

}