package com.pastpaperskenya.app.business.repository.main.profile

import android.net.Uri
import com.pastpaperskenya.app.business.model.user.Customer
import com.pastpaperskenya.app.business.model.user.UserDetails
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response

interface EditProfileRepository {
    suspend fun getUserDetails(userId: Int) : Flow<UserDetails?>?

    suspend fun updateUserToFirebase(userId: String, phone: String, firstname: String, lastname: String, country: String, county: String, email: String)

    suspend fun updateUserToDatabase(phone: String, firstname: String, lastname: String, country: String, county: String, userServerId: Int, photo: String?)

    suspend fun uploadProfileImage(file:  MultipartBody.Part, preset: RequestBody)

    suspend fun addImageToFirebaseStorage(imagename: String, imageUri: Uri): Flow<NetworkResult<Uri>>

    suspend fun addImageToFirebaseFirestore(uid: String, imageUrl: String): Flow<NetworkResult<Boolean>>

    suspend fun getImageFromFirebaseStorage(uid: String): Flow<NetworkResult<String>>

}