package com.pastpaperskenya.app.business.usecases

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pastpaperskenya.app.business.model.lipanampesa.PaymentDetails
import com.pastpaperskenya.app.business.model.user.UserDetails
import com.pastpaperskenya.app.business.model.user.UserDetails.Companion.toUserDetails
import com.pastpaperskenya.app.business.util.Constants
import kotlinx.coroutines.tasks.await

class FirestoreUserServiceImpl : FirestoreUserService {

    override suspend fun saveUserDetails(userDetails: UserDetails) {
        userDetails.userId?.let {
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
    ) {
        val user= hashMapOf(
            "phone" to phone,
            "firstname" to firstname,
            "lastname" to lastname,
            "country" to country,
            "county" to county,
        )

        Firebase.firestore.collection(Constants.FIREBASE_DATABASE_COLLECTION_USER).document(userId).update(
            user as Map<String, Any>
        )
    }

    override suspend fun getFirestoreUserDetails(userId: String): UserDetails? {
           return Firebase.firestore.collection(Constants.FIREBASE_DATABASE_COLLECTION_USER)
                .document(userId)
                .get().await().toUserDetails()

    }

    override suspend fun savePendingPaymentFirebase(paymentDetails: PaymentDetails) {
         Firebase.firestore.collection(Constants.FIREBASE_DATABASE_COLLECTION_PAYMENTS)
            .document(paymentDetails.checkoutRequestId)
             .set(paymentDetails).await()
    }

}