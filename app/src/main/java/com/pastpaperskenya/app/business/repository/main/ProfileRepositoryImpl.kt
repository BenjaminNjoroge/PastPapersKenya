package com.pastpaperskenya.app.business.repository.main

import com.pastpaperskenya.app.business.model.UserDetails
import com.pastpaperskenya.app.business.services.auth.UserService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val userService: UserService) : ProfileRepository{

    override suspend fun getUserDetails(userId: String): UserDetails? {
        return userService.getUserDetails(userId)
    }


}