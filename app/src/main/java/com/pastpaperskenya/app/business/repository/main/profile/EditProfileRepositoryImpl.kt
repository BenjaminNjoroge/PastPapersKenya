package com.pastpaperskenya.app.business.repository.main.profile

import com.pastpaperskenya.app.business.datasources.remote.BaseDataSource
import com.pastpaperskenya.app.business.datasources.remote.RemoteDataSource
import com.pastpaperskenya.app.business.model.user.Customer
import com.pastpaperskenya.app.business.model.user.UserDetails
import com.pastpaperskenya.app.business.usecases.FirestoreUserService
import com.pastpaperskenya.app.business.usecases.LocalUserService
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import com.pastpaperskenya.app.business.util.sealed.Resource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class EditProfileRepositoryImpl @Inject constructor(
    private val localUserService: LocalUserService,
    private val firestoreUserService: FirestoreUserService) :
    EditProfileRepository, BaseDataSource() {

    override suspend fun getUserDetails(userId: Int): Flow<UserDetails?> =
        localUserService.getUserFromDatabase(userId)


    override suspend fun updateUserToFirebase(userId: String, phone: String, firstname: String, lastname: String, country: String, county: String) {
        firestoreUserService.updateUserDetails(userId, phone, firstname, lastname, country, county)
    }

    override suspend fun updateUserToDatabase(phone: String, firstname: String, lastname: String, country: String, county: String, userServerId: Int) {
        localUserService.updateUserInDatabase(phone, firstname, lastname, country, county, userServerId)
    }


}