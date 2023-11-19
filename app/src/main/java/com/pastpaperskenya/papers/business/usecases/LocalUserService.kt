package com.pastpaperskenya.papers.business.usecases

import com.pastpaperskenya.papers.business.model.user.UserDetails
import kotlinx.coroutines.flow.Flow

interface LocalUserService {
    suspend fun insertUserToDatabase(userDetails: UserDetails):Long

    fun getUserFromDatabase(userServerId: Int): Flow<UserDetails>

    suspend fun updateUserInDatabase(phone: String, firstname: String, lastname: String, country: String, county: String, userServerId: Int, photo: String?)

    suspend fun deleteUserInLocalDatabase(userServerId: Int)
}