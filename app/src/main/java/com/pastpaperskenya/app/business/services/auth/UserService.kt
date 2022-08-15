package com.pastpaperskenya.app.business.services.auth

import com.pastpaperskenya.app.business.model.UserDetails

interface UserService {

    suspend fun saveUserDetails(userDetails: UserDetails)

    suspend fun updateUserDetails( userId:String,
                                   email: String?,
                                   phone: String?,
                                   firstname: String?,
                                   lastname: String?,
                                   country: String?,
                                   county: String?)

    suspend fun getUserDetails(userId: String):UserDetails

    suspend fun updateUserFcmToken(userId: String)
}