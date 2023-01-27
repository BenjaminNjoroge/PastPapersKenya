package com.pastpaperskenya.papers.business.repository.main.profile

import android.net.Uri
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pastpaperskenya.papers.business.datasources.remote.BaseDataSource
import com.pastpaperskenya.papers.business.datasources.remote.services.main.RetrofitApiService
import com.pastpaperskenya.papers.business.model.user.UserDetails
import com.pastpaperskenya.papers.business.usecases.FirestoreUserService
import com.pastpaperskenya.papers.business.usecases.LocalUserService
import com.pastpaperskenya.papers.business.util.Constants.BACKGROUND_IMAGE_NAME
import com.pastpaperskenya.papers.business.util.Constants.FIREBASE_DATABASE_COLLECTION_USER
import com.pastpaperskenya.papers.business.util.sealed.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


class EditProfileRepositoryImpl @Inject constructor(
    private val localUserService: LocalUserService,
    private val firestoreUserService: FirestoreUserService,
    private val retrofitApiService: RetrofitApiService,
    private val storage: FirebaseStorage,
    private val database: FirebaseFirestore) :

    EditProfileRepository, BaseDataSource() {

    override suspend fun getUserDetails(userId: Int): Flow<UserDetails?> =
        localUserService.getUserFromDatabase(userId)


    override suspend fun updateUserToFirebase(userId: String, phone: String, firstname: String, lastname: String, country: String, county: String, email: String) {
        firestoreUserService.updateUserDetails(userId, phone, firstname, lastname, country, county, email)
    }

    override suspend fun updateUserToDatabase(phone: String, firstname: String, lastname: String, country: String, county: String, userServerId: Int, photo: String?) {
        localUserService.updateUserInDatabase(phone, firstname, lastname, country, county, userServerId, photo)
    }

    override suspend fun uploadProfileImage(file:  MultipartBody.Part, preset: RequestBody) {
        safeApiCall { retrofitApiService.uploadProfileImage(file, preset) }
    }

    override suspend fun addImageToFirebaseStorage(imagename: String, imageUri: Uri): Flow<NetworkResult<Uri>> = flow {
        try {
            emit(NetworkResult.loading())
            val response= storage.reference.child(BACKGROUND_IMAGE_NAME).child(imagename)
                .putFile(imageUri).await().storage.downloadUrl.await()

            emit(NetworkResult.success(response))

        } catch (e: Exception){
            emit(NetworkResult.error(e.message.toString()))
        }
    }

    override suspend fun addImageToFirebaseFirestore(uid: String, imageUrl: String): Flow<NetworkResult<Boolean>> = flow{
        try {
            emit(NetworkResult.loading())
            database.collection(FIREBASE_DATABASE_COLLECTION_USER).document(uid).set(mapOf(
                "background_image_url" to imageUrl,
                "created_image_at" to FieldValue.serverTimestamp()
            )).await()
            emit(NetworkResult.success(true))
        } catch (e: Exception){
            emit(NetworkResult.error(e.message.toString()))
        }
    }

    override suspend fun getImageFromFirebaseStorage(uid: String): Flow<NetworkResult<String>> = flow{
        try {
            emit(NetworkResult.loading())

            val response= database.collection(FIREBASE_DATABASE_COLLECTION_USER).document(uid).get().await().getString("background_image_url")
            emit(NetworkResult.success(response))
        } catch (e: Exception){
            emit(NetworkResult.error(e.message.toString()))
        }
    }

}