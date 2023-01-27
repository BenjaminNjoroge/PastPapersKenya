package com.pastpaperskenya.papers.presentation.main.profile.reset

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.pastpaperskenya.papers.business.util.AuthEvents
import com.pastpaperskenya.papers.business.repository.auth.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(private val repository: FirebaseAuthRepository) : ViewModel(){

    private  val TAG = "ResetPasswordViewModel"

    private var _eventsChannel= Channel<AuthEvents>()
    val resetChannel= _eventsChannel.receiveAsFlow()

    private var _firebaseUser= MutableLiveData<FirebaseUser?>()
    val firebaseUser get() = _firebaseUser

    fun resetPassword(email: String)= viewModelScope.launch {
        when{
            email.isEmpty()->{
                _eventsChannel.send(AuthEvents.ErrorCode(1))
            }
            else->{
                actualResetPassword(email)
            }
        }
    }

    private fun actualResetPassword(email: String)= viewModelScope.launch {
        try{
            val result= repository.sendResetPassword(email)
            if (result){
                _eventsChannel.send(AuthEvents.Message("A reset email has been sent to $email. \n Please check your inbox or spam folder of your email"))
            } else{
                _eventsChannel.send(AuthEvents.Error("Email not sent"))
            }
        }catch (e: Exception){
            _eventsChannel.send(AuthEvents.Error(e.message.toString()))
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