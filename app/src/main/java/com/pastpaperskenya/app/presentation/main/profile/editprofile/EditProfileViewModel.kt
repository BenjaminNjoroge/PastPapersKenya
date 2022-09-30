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
import com.pastpaperskenya.app.business.datasources.remote.services.auth.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val repository: FirebaseRepository,
    private val userService: UserService
) : ViewModel() {

    private val _loading= MutableLiveData(false)
    val loading get() = _loading

    private val _userProfile= MutableLiveData<UserDetails>()
    val userProfile : LiveData<UserDetails> = _userProfile

    private var _firebaseUser= MutableLiveData<FirebaseUser?>()
    val firebaseUser get() = _firebaseUser

    private var _eventsChannel= Channel<AuthEvents>()
    val authEventsChannel= _eventsChannel.receiveAsFlow()

    init {
        val firebaseAuth= FirebaseAuth.getInstance()
        val user= firebaseAuth.currentUser?.uid

        viewModelScope.launch {
            _loading.postValue(true)
            _userProfile.value= userService.getUserDetails(user!!)
            _loading.postValue(false)
        }
    }

    fun updateUserDetails(userId: String, phone: String, firstname: String, lastname :String, country:String, county: String, userServerId:String)= viewModelScope.launch {
        userService.updateUserDetails(userId, phone, firstname, lastname, country, county, userServerId)
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