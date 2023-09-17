package com.pastpaperskenya.papers.presentation.main.profile.editprofile

import android.net.Uri
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import com.pastpaperskenya.papers.business.model.user.CustomerUpdate
import com.pastpaperskenya.papers.business.model.user.UserDetails
import com.pastpaperskenya.papers.business.util.AuthEvents
import com.pastpaperskenya.papers.business.repository.auth.FirebaseAuthRepository
import com.pastpaperskenya.papers.business.repository.main.user.ServerCrudRepository
import com.pastpaperskenya.papers.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.papers.business.repository.main.profile.EditProfileRepository
import com.pastpaperskenya.papers.business.util.Constants
import com.pastpaperskenya.papers.business.util.convertIntoNumeric
import com.pastpaperskenya.papers.business.util.network.NetworkChangeReceiver
import com.pastpaperskenya.papers.business.util.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val editProfileRepository: EditProfileRepository,
    private val datastore: DataStoreRepository,
    private val serverCrudRepository: ServerCrudRepository
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading get() = _loading

    private val _userProfile = MutableLiveData<UserDetails>()
    val userProfile: LiveData<UserDetails> = _userProfile


    private var _firebaseUser = MutableLiveData<FirebaseUser?>()
    val firebaseUser get() = _firebaseUser

    private var eventsChannel = Channel<AuthEvents>()
    val events = eventsChannel.receiveAsFlow()

    private var _userServerId = MutableLiveData<String>()
    val userServerId: LiveData<String> = _userServerId

    private var _addImageStorageResponse= MutableLiveData<NetworkResult<Uri>>()
    val addImageStorageResponse: LiveData<NetworkResult<Uri>> = _addImageStorageResponse

    private var _addImageDatabaseResponse= MutableLiveData<NetworkResult<Boolean>>()
    val addImageDatabaseResponse: LiveData<NetworkResult<Boolean>> = _addImageDatabaseResponse

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val id = datastore.getValue(Constants.USER_SERVER_ID)

            try {
                getUserDetails(convertIntoNumeric(id!!))
            } catch (e: Exception) {
                eventsChannel.send(AuthEvents.Error("Unable to get data $e"))
            }
        }
    }

    fun addBackgroundImageToStorage(imagename: String, imageuri: Uri)= viewModelScope.launch {
        editProfileRepository.addImageToFirebaseStorage(imagename, imageuri).collect{
            _addImageStorageResponse.postValue(it)
        }
    }

    fun addBackgoundImageToFirestore(uid: String, imageUrl: String)= viewModelScope.launch {
        editProfileRepository.addImageToFirebaseFirestore(uid, imageUrl).collect{
            _addImageDatabaseResponse.postValue(it)
        }
    }



    fun updateFirestoreDetails(userId: String, phone: String, firstname: String, lastname: String, country: String, county: String, email: String) = viewModelScope.launch {
        editProfileRepository.updateUserToFirebase(userId, phone, firstname, lastname, country, county, email)
    }

    fun updateLocalDetails(phone: String, firstname: String, lastname: String, country: String, county: String, userServerId: Int, photo: String?) = viewModelScope.launch {
        editProfileRepository.updateUserToDatabase(phone, firstname, lastname, country, county, userServerId, photo)
    }


    fun fieldsChecker(userServerId: Int, phone: String, firstname: String, lastname: String, customer: CustomerUpdate
    ) = viewModelScope.launch {
        when {
            firstname.isEmpty() -> {
                eventsChannel.send(AuthEvents.ErrorCode(1))
            }
            lastname.isEmpty() -> {
                eventsChannel.send(AuthEvents.ErrorCode(2))
            }
            phone.isEmpty() -> {
                eventsChannel.send(AuthEvents.ErrorCode(3))
            }

            else -> {
                if(NetworkChangeReceiver.isNetworkConnected()){
                    updateServerDetails(userServerId, customer)
                } else{
                    eventsChannel.send(AuthEvents.Message("Error. Network not available"))
                }
            }
        }
    }

    private suspend fun updateServerDetails(
        userServerId: Int,
        customer: CustomerUpdate
    ) = viewModelScope.launch {
        try {
            val response= serverCrudRepository.updateUser(userServerId, customer)

            if (response.isSuccessful) {
                eventsChannel.send(AuthEvents.Message("Updated successfully"))
                eventsChannel.send(AuthEvents.ErrorCode(100))

            } else{
                val error= response.errorBody().toString()
                eventsChannel.send(AuthEvents.Message("$error: An error occurred. check your internet bundles"))
            }
        } catch (e: Exception){
            eventsChannel.send(AuthEvents.Message("$e An error occured"))
        }
    }

    private suspend fun getUserDetails(userServerId: Int) {
        editProfileRepository.getUserDetails(userServerId)?.collect {
            _userProfile.postValue(it)
        }
    }

    fun logout() = viewModelScope.launch {
        try {
            val user = firebaseAuthRepository.signOut()
            user?.let {
                eventsChannel.send(AuthEvents.Message("logout failure"))
            } ?: eventsChannel.send(AuthEvents.Message("Logout Success"))

            getCurrentUser()

        } catch (e: Exception) {
            eventsChannel.send(AuthEvents.Error(e.message.toString()))
        }
    }

    private fun getCurrentUser() = viewModelScope.launch {
        val user = firebaseAuthRepository.getCurrentUser()
        _firebaseUser.postValue(user)
    }


}