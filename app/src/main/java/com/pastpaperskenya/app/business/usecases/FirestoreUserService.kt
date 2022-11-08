package com.pastpaperskenya.app.business.usecases

import com.pastpaperskenya.app.business.model.auth.UserDetails

interface FirestoreUserService {

    suspend fun saveUserDetails(userDetails: UserDetails)

    suspend fun updateUserDetails(
        userId: String,
        phone: String?,
        firstname: String?,
        lastname: String?,
        country: String?,
        county: String?,
        userServerId: Int?
    )

    //suspend fun getUserDetails(userId: String): UserDetails?

    //suspend fun updateUserFcmToken(userId: String)
}