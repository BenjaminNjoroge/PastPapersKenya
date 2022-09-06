package com.pastpaperskenya.app.business.services.auth

import com.pastpaperskenya.app.business.model.auth.UserDetails

interface UserService {

    suspend fun saveUserDetails(userDetails: UserDetails)

    suspend fun updateUserDetails( userId: String,
                                   phone: String?,
                                   firstname: String?,
                                   lastname: String?,
                                   country: String?,
                                   county: String?,
                                   userServerId: String?)

    suspend fun getUserDetails(userId: String): UserDetails?

    suspend fun updateUserFcmToken(userId: String)
}