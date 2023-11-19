package com.pastpaperskenya.papers.business.repository.main.profile

import com.pastpaperskenya.papers.business.model.user.UserDetails
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun getUserDetailsLocally(userId: Int): Flow<UserDetails?>
}