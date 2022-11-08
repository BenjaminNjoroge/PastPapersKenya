package com.pastpaperskenya.app.business.repository.main.profile

import com.pastpaperskenya.app.business.model.auth.UserDetails
import com.pastpaperskenya.app.business.usecases.FirestoreUserService
import javax.inject.Inject

class UserDetailsRepositoryImpl @Inject constructor
    (private val firestoreUserService: FirestoreUserService): UserDetailsRepository {

    override suspend fun userDetails(
        userId: String,
        email: String,
        phone: String,
        firstname: String,
        lastname: String,
        county: String,
        country: String,
        userServerId: Int
    ) {
        firestoreUserService.saveUserDetails(UserDetails(userId, email, phone, firstname, lastname, country, county, userServerId))
    }
}