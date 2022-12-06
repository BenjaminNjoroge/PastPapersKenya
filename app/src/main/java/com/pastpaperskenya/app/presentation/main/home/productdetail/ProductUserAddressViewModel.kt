package com.pastpaperskenya.app.presentation.main.home.productdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pastpaperskenya.app.business.model.user.CustomerUpdate
import com.pastpaperskenya.app.business.model.user.UserDetails
import com.pastpaperskenya.app.business.util.AuthEvents
import com.pastpaperskenya.app.business.repository.main.user.ServerCrudRepository
import com.pastpaperskenya.app.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.app.business.repository.main.profile.EditProfileRepository
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.convertIntoNumeric
import com.pastpaperskenya.app.business.util.network.NetworkChangeReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductUserAddressViewModel @Inject constructor(
    private val editProfileRepository: EditProfileRepository,
    private val datastore: DataStoreRepository,
    private val serverCrudRepository: ServerCrudRepository
): ViewModel() {

    private val _userProfile = MutableLiveData<UserDetails>()
    val userProfile: LiveData<UserDetails> = _userProfile

    private var _userServerId = MutableLiveData<String>()
    val userServerId: LiveData<String> = _userServerId


    private var eventsChannel = Channel<AuthEvents>()
    val events = eventsChannel.receiveAsFlow()

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


    fun updateFirestoreDetails(
        userId: String,
        phone: String,
        firstname: String,
        lastname: String,
        country: String,
        county: String
    ) = viewModelScope.launch {
        editProfileRepository.updateUserToFirebase(
            userId,
            phone,
            firstname,
            lastname,
            country,
            county
        )
    }

    fun updateLocalDetails(
        phone: String,
        firstname: String,
        lastname: String,
        country: String,
        county: String,
        userServerId: Int,
        photo: String?
    ) = viewModelScope.launch {
        editProfileRepository.updateUserToDatabase(
            phone,
            firstname,
            lastname,
            country,
            county,
            userServerId,
            photo
        )
    }


    fun fieldsChecker(
        userServerId: Int,
        phone: String,
        firstname: String,
        lastname: String,
        customer: CustomerUpdate
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
}