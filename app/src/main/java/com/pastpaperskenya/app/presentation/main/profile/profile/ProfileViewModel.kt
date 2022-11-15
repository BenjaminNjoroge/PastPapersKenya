package com.pastpaperskenya.app.presentation.main.profile.profile

import android.util.Log
import androidx.lifecycle.*
import com.pastpaperskenya.app.business.model.user.UserDetails
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.app.business.repository.main.profile.ProfileRepository
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.convertIntoNumeric
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ProfileViewModel"

@HiltViewModel
class ProfileViewModel @Inject constructor
    (
    private val profileRepository: ProfileRepository,
    private val datastore: DataStoreRepository
) : ViewModel() {

    private val _userProfile = MutableLiveData<UserDetails>()
    val userProfile: LiveData<UserDetails> = _userProfile

    private var eventsChannel = Channel<AuthEvents>()
    val events = eventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val id = datastore.getValue(Constants.USER_SERVER_ID)
            try {
                getUserDetails(convertIntoNumeric(id!!))
            } catch (e: Exception) {
                eventsChannel.send(AuthEvents.Error("Unable to get data $e"))
            }
        }

    }


    private suspend fun getUserDetails(userServerId: Int) =
        profileRepository.getUserDetails(userServerId).catch { e ->
            Log.d(TAG, "getUserDetails: $e")
        }.collect {
            _userProfile.postValue(it)
        }

}