package com.pastpaperskenya.app.business.repository.main.profile

import com.pastpaperskenya.app.business.model.auth.UserDetails

interface ProfileRepository {

    suspend fun getUserDetails(userId: String): UserDetails?
}