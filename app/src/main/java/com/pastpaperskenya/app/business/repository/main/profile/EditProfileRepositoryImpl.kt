package com.pastpaperskenya.app.business.repository.main.profile

import com.pastpaperskenya.app.business.datasources.remote.BaseDataSource
import com.pastpaperskenya.app.business.datasources.remote.services.main.RetrofitApiService
import com.pastpaperskenya.app.business.model.user.UserDetails
import com.pastpaperskenya.app.business.usecases.FirestoreUserService
import com.pastpaperskenya.app.business.usecases.LocalUserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


class EditProfileRepositoryImpl @Inject constructor(
    private val localUserService: LocalUserService,
    private val firestoreUserService: FirestoreUserService,
private val retrofitApiService: RetrofitApiService) :
    EditProfileRepository, BaseDataSource() {

    override suspend fun getUserDetails(userId: Int): Flow<UserDetails?> =
        localUserService.getUserFromDatabase(userId)


    override suspend fun updateUserToFirebase(userId: String, phone: String, firstname: String, lastname: String, country: String, county: String) {
        firestoreUserService.updateUserDetails(userId, phone, firstname, lastname, country, county)
    }

    override suspend fun updateUserToDatabase(phone: String, firstname: String, lastname: String, country: String, county: String, userServerId: Int, photo: String?) {
        localUserService.updateUserInDatabase(phone, firstname, lastname, country, county, userServerId, photo)
    }

    override suspend fun uploadProfileImage(file:  MultipartBody.Part, preset: RequestBody) {
        safeApiCall { retrofitApiService.uploadProfileImage(file, preset) }
    }


}