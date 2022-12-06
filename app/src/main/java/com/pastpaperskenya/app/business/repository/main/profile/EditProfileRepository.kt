package com.pastpaperskenya.app.business.repository.main.profile

import com.pastpaperskenya.app.business.model.user.Customer
import com.pastpaperskenya.app.business.model.user.UserDetails
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface EditProfileRepository {
    suspend fun getUserDetails(userId: Int) : Flow<UserDetails?>?

    suspend fun updateUserToFirebase(userId: String, phone: String, firstname: String, lastname: String, country: String, county: String)

    suspend fun updateUserToDatabase(phone: String, firstname: String, lastname: String, country: String, county: String, userServerId: Int, photo: String?)

    suspend fun uploadProfileImage(file:  MultipartBody.Part, preset: RequestBody)
}