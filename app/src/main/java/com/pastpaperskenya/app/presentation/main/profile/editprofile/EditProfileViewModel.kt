package com.pastpaperskenya.app.presentation.main.profile.editprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseUser
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.business.repository.auth.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val repository: FirebaseRepository) : ViewModel() {

    private var _firebaseUser= MutableLiveData<FirebaseUser?>()
    val firebaseUser get() = _firebaseUser

    private var _eventsChannel= Channel<AuthEvents>()
    val authEventsChannel= _eventsChannel.receiveAsFlow()

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