package com.pastpaperskenya.app.business.repository.main

import com.pastpaperskenya.app.business.model.UserDetails
import com.pastpaperskenya.app.business.services.auth.UserService
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun getUserDetails(userId: String): UserDetails?
}