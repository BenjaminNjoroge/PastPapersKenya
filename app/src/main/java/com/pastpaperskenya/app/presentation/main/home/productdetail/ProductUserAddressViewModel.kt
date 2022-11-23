package com.pastpaperskenya.app.presentation.main.home.productdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pastpaperskenya.app.business.model.user.Customer
import com.pastpaperskenya.app.business.model.user.UserDetails
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.app.business.repository.main.profile.EditProfileRepository
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.convertIntoNumeric
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductUserAddressViewModel @Inject constructor(
    private val editProfileRepository: EditProfileRepository,
    private val datastore: DataStoreRepository
): ViewModel() {

    private val _userProfile = MutableLiveData<UserDetails>()
    val userProfile: LiveData<UserDetails> = _userProfile

    private var _userServerId = MutableLiveData<String>()
    val userServerId: LiveData<String> = _userServerId

    private var _updateServerDetails = MutableLiveData<NetworkResult<Customer>>()
    val updateServerDetails: LiveData<NetworkResult<Customer>> = _updateServerDetails

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
        userServerId: Int
    ) = viewModelScope.launch {
        editProfileRepository.updateUserToDatabase(
            phone,
            firstname,
            lastname,
            country,
            county,
            userServerId
        )
    }


    fun fieldsChecker(
        userServerId: Int,
        phone: String,
        firstname: String,
        lastname: String,
        country: String,
        county: String
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
                updateServerDetails(userServerId, phone, firstname, lastname, country, county)
            }
        }
    }

    private suspend fun updateServerDetails(
        userServerId: Int,
        phone: String,
        firstname: String,
        lastname: String,
        country: String,
        county: String
    ) = viewModelScope.launch {
        editProfileRepository.updateUserToServer(
            userServerId,
            firstname,
            lastname,
            phone,
            country,
            county
        ).collect {
            _updateServerDetails.value = it
            delay(2000)
            eventsChannel.send(AuthEvents.Message("Updated successfully"))
            eventsChannel.send(AuthEvents.ErrorCode(100))
        }
    }

    private suspend fun getUserDetails(userServerId: Int) {
        editProfileRepository.getUserDetails(userServerId)?.collect {
            _userProfile.postValue(it)
        }
    }
}