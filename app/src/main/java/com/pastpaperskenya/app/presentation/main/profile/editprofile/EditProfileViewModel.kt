package com.pastpaperskenya.app.presentation.main.profile.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pastpaperskenya.app.business.model.auth.UserDetails
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.business.repository.auth.FirebaseRepository
import com.pastpaperskenya.app.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.app.business.usecases.FirestoreUserService
import com.pastpaperskenya.app.business.usecases.LocalUserService
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.convertIntoNumeric
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val repository: FirebaseRepository,
    private val firestoreUserService: FirestoreUserService,
    private val localUserService: LocalUserService,
    private val datastore:DataStoreRepository
) : ViewModel() {

    private val _loading= MutableLiveData(false)
    val loading get() = _loading

    private val _userProfile= MutableLiveData<UserDetails>()
    val userProfile : LiveData<UserDetails> = _userProfile

    val _userServerId= MutableLiveData<String?>()

    private var _firebaseUser= MutableLiveData<FirebaseUser?>()
    val firebaseUser get() = _firebaseUser

    private var _eventsChannel= Channel<AuthEvents>()
    val authEventsChannel= _eventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val id= datastore.getValue(Constants.USER_SERVER_ID)
            getUserDetails(convertIntoNumeric(id!!))
        }
    }


    fun updateUserDetails(userId: String, phone: String, firstname: String, lastname :String, country:String, county: String, userServerId:Int)= viewModelScope.launch {
        firestoreUserService.updateUserDetails(userId, phone, firstname, lastname, country, county, userServerId)
        localUserService.updateUserInDatabase(phone, firstname, lastname, country, county, userServerId)
    }

    private suspend fun getUserDetails(userServerId: Int){
        localUserService.getUserFromDatabase(userServerId).collect{
            _userProfile.postValue(it)
        }
    }

    fun logout()= viewModelScope.launch {
        try {
            val user= repository.signOut()
            user?.let {
                _eventsChannel.send(AuthEvents.Message("logout failure"))
            }?: _eventsChannel.send(AuthEvents.Message("Logout Success"))

            getCurrentUser()

        } catch (e: Exception){
            _eventsChannel.send(AuthEvents.Error(e.message.toString()))
        }
    }

    private fun getCurrentUser()= viewModelScope.launch {
        val user= repository.getCurrentUser()
        _firebaseUser.postValue(user)
    }


}