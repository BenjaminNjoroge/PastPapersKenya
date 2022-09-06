package com.pastpaperskenya.app.business.repository.main.profile

import com.pastpaperskenya.app.business.model.auth.UserDetails
import com.pastpaperskenya.app.business.services.auth.UserService
import javax.inject.Inject

class EditProfileRepositoryImpl @Inject constructor (private val userService: UserService):
    EditProfileRepository {

    override suspend fun getUserDetails(userId: String): UserDetails? {
        return userService.getUserDetails(userId)
    }

    override suspend fun updateUserDetails(
        userId: String,
        firstname: String,
        lastname: String,
        phone: String,
        country: String,
        county: String,
        userServerId: String
    ) {
        userService.updateUserDetails(userId, phone, firstname, lastname, country, county, userServerId)
    }


}