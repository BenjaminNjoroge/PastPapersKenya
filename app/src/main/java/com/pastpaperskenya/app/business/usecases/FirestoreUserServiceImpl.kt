package com.pastpaperskenya.app.business.usecases

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.pastpaperskenya.app.business.model.auth.UserDetails
import com.pastpaperskenya.app.business.model.lipanampesa.DatabaseKeys
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
        userServerId: Int?
    ) {
        val user= hashMapOf(
            "phone" to phone,
            "firstname" to firstname,
            "lastname" to lastname,
            "country" to country,
            "county" to county,
            "userServerId" to userServerId
        )

        Firebase.firestore.collection(Constants.FIREBASE_DATABASE_COLLECTION_USER).document(userId).update(
            user as Map<String, Any>
        )
    }

//    override suspend fun getUserDetails(userId: String): UserDetails? {
//           return Firebase.firestore.collection(Constants.FIREBASE_DATABASE_COLLECTION_USER)
//                .document(userId)
//                .get().await().toUserDetails()
//
//    }

//    override suspend fun updateUserFcmToken(userId: String) {
//        val token= FirebaseMessaging.getInstance().token
//        val data= mapOf(
//            DatabaseKeys.User.fcmToken to token
//        )
//        Firebase.firestore.collection(Constants.FIREBASE_DATABASE_COLLECTION_USER).document(userId).update(data).await()
//    }
}