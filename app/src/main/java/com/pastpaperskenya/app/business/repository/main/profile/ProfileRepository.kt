package com.pastpaperskenya.app.business.repository.main.profile

import com.pastpaperskenya.app.business.model.auth.UserDetails
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun getUserDetails(userId: Int): Flow<UserDetails?>
}