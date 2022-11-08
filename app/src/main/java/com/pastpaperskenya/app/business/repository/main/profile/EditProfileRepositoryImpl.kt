package com.pastpaperskenya.app.business.repository.main.profile

import com.pastpaperskenya.app.business.model.auth.UserDetails
import com.pastpaperskenya.app.business.usecases.FirestoreUserService
import com.pastpaperskenya.app.business.usecases.LocalUserService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EditProfileRepositoryImpl @Inject constructor(
    private val localUserService: LocalUserService,
    private val firestoreUserService: FirestoreUserService
) :
    EditProfileRepository {

    override suspend fun getUserDetails(userId: Int): Flow<UserDetails?> =
        localUserService.getUserFromDatabase(userId)


    override suspend fun updateUserToFirebase(userId: String, firstname: String, lastname: String, phone: String, country: String, county: String) {
        firestoreUserService.updateUserDetails(userId, phone, firstname, lastname, country, county)

    }

    override suspend fun updateUserToDatabase(userId: String, firstname: String, lastname: String, phone: String, country: String, county: String, userServerId: Int) {
        localUserService.updateUserInDatabase(phone, firstname, lastname, country, county, userServerId)
    }


}