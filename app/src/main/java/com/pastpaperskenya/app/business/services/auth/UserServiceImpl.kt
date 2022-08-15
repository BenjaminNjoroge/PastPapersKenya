package com.pastpaperskenya.app.business.services.auth

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.pastpaperskenya.app.business.model.UserDetails
import com.pastpaperskenya.app.business.model.lipanampesa.DatabaseKeys
import com.pastpaperskenya.app.business.util.Constants
import kotlinx.coroutines.tasks.await

class UserServiceImpl : UserService {

    override suspend fun saveUserDetails(userDetails: UserDetails) {
        Firebase.firestore.collection(Constants.FIREBASE_DATABASE_COLLECTION_USER).document(userDetails.userId).set(userDetails).await()
    }

    override suspend fun updateUserDetails(
        userId: String,
        email: String?,
        phone: String?,
        firstname: String?,
        lastname: String?,
        country: String?,
        county: String?
    ) {
        val userDetailsMap= mapOf(
            DatabaseKeys.User.firstname to firstname,
            DatabaseKeys.User.lastname to lastname,
            DatabaseKeys.User.phone to phone,
            DatabaseKeys.User.county to county,
            DatabaseKeys.User.country to country
        )
        Firebase.firestore.collection(Constants.FIREBASE_DATABASE_COLLECTION_USER).document(userId).update(userDetailsMap)
    }

    override suspend fun getUserDetails(userId: String): UserDetails {
        return Firebase.firestore
            .collection(Constants.FIREBASE_DATABASE_COLLECTION_USER)
            .document(userId)
            .get().await()
            .let { snapshot->
                UserDetails(
                    snapshot[DatabaseKeys.User.userId] as String,
                    snapshot[DatabaseKeys.User.email] as String,
                    snapshot[DatabaseKeys.User.firstname] as String,
                    snapshot[DatabaseKeys.User.lastname] as String,
                    snapshot[DatabaseKeys.User.phone] as String,
                    snapshot[DatabaseKeys.User.country] as String,
                    snapshot[DatabaseKeys.User.county] as String
                )
            }
    }

    override suspend fun updateUserFcmToken(userId: String) {
        val token= FirebaseMessaging.getInstance().token
        val data= mapOf(
            DatabaseKeys.User.fcmToken to token
        )
        Firebase.firestore.collection(Constants.FIREBASE_DATABASE_COLLECTION_USER).document(userId).update(data).await()
    }
}