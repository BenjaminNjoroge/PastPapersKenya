package com.pastpaperskenya.app.business.repository.main

import com.pastpaperskenya.app.business.model.UserDetails
import com.pastpaperskenya.app.business.services.auth.UserService
import javax.inject.Inject

class UserDetailsRepositoryImpl @Inject constructor
    (private val userService: UserService): UserDetailsRepository {

    override suspend fun userDetails(
        userId: String,
        email: String,
        phone: String,
        firstname: String,
        lastname: String,
        county: String,
        country: String
    ) {
        userService.saveUserDetails(UserDetails(userId, email, phone, firstname, lastname, country, county))
    }
}