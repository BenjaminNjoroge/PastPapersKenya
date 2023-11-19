package com.pastpaperskenya.papers.business.repository.main.profile

import com.pastpaperskenya.papers.business.model.user.UserDetails
import com.pastpaperskenya.papers.business.usecases.LocalUserService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val localUserService: LocalUserService
) : ProfileRepository {

    override suspend fun getUserDetailsLocally(userId: Int) :Flow<UserDetails> = localUserService.getUserFromDatabase(userId)


}