package com.pastpaperskenya.papers.presentation.main.profile.profile

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pastpaperskenya.papers.business.model.user.UserDetails
import com.pastpaperskenya.papers.business.repository.auth.FirebaseAuthRepository
import com.pastpaperskenya.papers.business.util.AuthEvents
import com.pastpaperskenya.papers.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.papers.business.repository.main.profile.EditProfileRepository
import com.pastpaperskenya.papers.business.repository.main.profile.ProfileRepository
import com.pastpaperskenya.papers.business.usecases.FirestoreUserService
import com.pastpaperskenya.papers.business.util.Constants
import com.pastpaperskenya.papers.business.util.convertIntoNumeric
import com.pastpaperskenya.papers.business.util.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val datastore: DataStoreRepository,
    private val editProfileRepository: EditProfileRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val firestoreUserService: FirestoreUserService,

    ) : ViewModel() {

    private val _userProfile = MutableLiveData<UserDetails>()
    val userProfile: LiveData<UserDetails> = _userProfile

    private val _backgroundImageResponse= MutableLiveData<NetworkResult<String>>()
    val backgroundImageResponse: LiveData<NetworkResult<String>> = _backgroundImageResponse

    private var eventsChannel = Channel<AuthEvents>()
    val events = eventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val auth= FirebaseAuth.getInstance()
            val user= auth.currentUser
            val email= user?.email.toString()
            val id = datastore.getValue(Constants.USER_SERVER_ID)
            if (id== null){
                val newId= firestoreUserService.getFirestoreUserDetails(email)?.userServerId
                try {
                    newId?.let { getUserDetails(it) }
                } catch (e: Exception) {
                    eventsChannel.send(AuthEvents.Error("Unable to get data $e"))
                }
            } else{
                try {
                    getUserDetails(convertIntoNumeric(id))
                } catch (e: Exception) {
                    eventsChannel.send(AuthEvents.Error("Unable to get data $e"))
                }
            }

        }

    }

    init {
        viewModelScope.launch {
            val uid = firebaseAuthRepository.getCurrentUser()?.uid

            getBackgroundImageFromStorage(uid.toString())
        }
    }


    private suspend fun getUserDetails(userServerId: Int) =
        profileRepository.getUserDetailsLocally(userServerId).catch { e ->
            Log.d(TAG, "getUserDetails: $e")
        }.collect {
            _userProfile.postValue(it)
        }

    private suspend fun getBackgroundImageFromStorage(uid: String)=
        editProfileRepository.getImageFromFirebaseStorage(uid).collect{
            _backgroundImageResponse.postValue(it)
        }

}