package com.pastpaperskenya.papers.business.repository.main.profile

import com.pastpaperskenya.papers.business.model.user.UserDetails
import com.pastpaperskenya.papers.business.usecases.FirestoreUserService
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
        userServerId: Int,
        profileImage: String
    ) {
        firestoreUserService.saveUserDetails(UserDetails(userId, email, phone, firstname, lastname, country, county, userServerId, profileImage))
    }
}