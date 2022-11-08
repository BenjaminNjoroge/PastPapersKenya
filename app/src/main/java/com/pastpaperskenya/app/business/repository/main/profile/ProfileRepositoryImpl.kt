package com.pastpaperskenya.app.business.repository.main.profile

import com.pastpaperskenya.app.business.model.auth.UserDetails
import com.pastpaperskenya.app.business.usecases.FirestoreUserService
import com.pastpaperskenya.app.business.usecases.LocalUserService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val localUserService: LocalUserService
) : ProfileRepository {

    override suspend fun getUserDetails(userId: Int) :Flow<UserDetails> = localUserService.getUserFromDatabase(userId)


}