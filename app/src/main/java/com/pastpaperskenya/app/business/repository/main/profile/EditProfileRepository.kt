package com.pastpaperskenya.app.business.repository.main.profile

import com.pastpaperskenya.app.business.model.auth.UserDetails

interface EditProfileRepository {
    suspend fun getUserDetails(userId: String) : UserDetails?
    suspend fun updateUserDetails(userId: String,firstname: String, lastname: String, phone: String, country: String, county: String, userServerId: String)
}