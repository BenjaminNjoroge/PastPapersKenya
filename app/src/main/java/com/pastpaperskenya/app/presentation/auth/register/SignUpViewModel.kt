package com.pastpaperskenya.app.presentation.auth.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.pastpaperskenya.app.business.model.UserDetails
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.business.repository.auth.FirebaseRepository
import com.pastpaperskenya.app.business.services.auth.UserService

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor
    (private val repository: FirebaseRepository,
private val userService: UserService) :ViewModel() {

    private  val TAG = "SignUpViewModel"

    private var _firebaseUser= MutableLiveData<FirebaseUser?>()
    val firebaseUser get() = _firebaseUser

    private var eventsChannel= Channel<AuthEvents>()
    val authEventsFlow= eventsChannel.receiveAsFlow()

    fun register(email: String,
                 phone: String,
                 firstname: String,
                 lastname: String,
                 country: String,
                 county: String,
                 password: String,
                 confirmPassword: String)= viewModelScope.launch {
        when{
            firstname.isEmpty()->{
                eventsChannel.send(AuthEvents.ErrorCode(1))
            }
            lastname.isEmpty()->{
                eventsChannel.send(AuthEvents.ErrorCode(2))
            }
            email.isEmpty()->{
                eventsChannel.send(AuthEvents.ErrorCode(3))
            }
            password.isEmpty()->{
                eventsChannel.send(AuthEvents.ErrorCode(4))
            }
            password!=confirmPassword || confirmPassword.isEmpty()->{
                eventsChannel.send(AuthEvents.ErrorCode(5))
            }

            else->{
                actualRegisterUser(email, phone, firstname, lastname, country, county, password)
            }
        }
    }

    private fun actualRegisterUser(email: String, phone: String, firstname: String, lastname: String, country: String, county: String, password: String) = viewModelScope.launch {

        try {
            val user= repository.signUpWithEmailPassword(email, password)
            user?.let{
                _firebaseUser.postValue(it)
                eventsChannel.send(AuthEvents.Message("User Registered Successfully"))
                val userIds= user.uid
                userService.saveUserDetails(UserDetails(userIds, email, phone, firstname, lastname, country, county))
                eventsChannel.send(AuthEvents.Message("Success \n User saved to database"))

            }
        }catch (e: Exception){
            eventsChannel.send(AuthEvents.Error(e.message.toString()))
        }
    }

}