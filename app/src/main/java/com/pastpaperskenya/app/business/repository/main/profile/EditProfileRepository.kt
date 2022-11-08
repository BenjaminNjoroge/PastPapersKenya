package com.pastpaperskenya.app.business.repository.main.profile

import com.pastpaperskenya.app.business.model.auth.UserDetails
import kotlinx.coroutines.flow.Flow

interface EditProfileRepository {
    suspend fun getUserDetails(userId: Int) : Flow<UserDetails?>?
    suspend fun updateUserToFirebase(userId: String,firstname: String, lastname: String, phone: String, country: String, county: String, userServerId: Int)

    suspend fun updateUserToDatabase(userId: String,firstname: String, lastname: String, phone: String, country: String, county: String, userServerId: Int)

}