package com.pastpaperskenya.app.business.repository.main.profile

import com.pastpaperskenya.app.business.model.auth.UserDetails
import com.pastpaperskenya.app.business.datasources.remote.services.auth.UserService
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val userService: UserService
) : ProfileRepository {

    override suspend fun getUserDetails(userId: String): UserDetails? {
        return userService.getUserDetails(userId)
    }


}