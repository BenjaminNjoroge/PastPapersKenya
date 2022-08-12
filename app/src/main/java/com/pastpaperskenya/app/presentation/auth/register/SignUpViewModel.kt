package com.pastpaperskenya.app.presentation.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.business.repository.auth.FirebaseRepository
import com.pastpaperskenya.app.business.util.Result

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val repository: FirebaseRepository) :ViewModel() {

    private  val TAG = "SignUpViewModel"

    private var _firebaseUser= MutableLiveData<FirebaseUser?>()
    val firebaseUser get() = _firebaseUser

    private var eventsChannel= Channel<AuthEvents>()
    val authEventsFlow= eventsChannel.receiveAsFlow()

    fun register(email: String, password: String, confirmPassword: String)= viewModelScope.launch {
        when{
            email.isEmpty()->{
                eventsChannel.send(AuthEvents.ErrorCode(1))
            }
            password.isEmpty()->{
                eventsChannel.send(AuthEvents.ErrorCode(2))
            }

            password!=confirmPassword && confirmPassword.isEmpty()->{
                eventsChannel.send(AuthEvents.ErrorCode(3))
            }
            else->{
                actualRegisterUser(email, password)
            }
        }
    }

    private fun actualRegisterUser(email: String, password: String) = viewModelScope.launch {

        try {
            val user= repository.signUpWithEmailPassword(email, password)
            user?.let{
                _firebaseUser.postValue(it)
                eventsChannel.send(AuthEvents.Message("User Registered Successfully"))
            }
        }catch (e: Exception){
            eventsChannel.send(AuthEvents.Error(e.message.toString()))
        }
    }

}