package com.pastpaperskenya.app.presentation.auth.login

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.business.repository.auth.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor (private val repository: FirebaseRepository): ViewModel(){

    private  val TAG = "SignInViewModel"
    private var auth: FirebaseAuth= Firebase.auth

    private var _firebaseUser= MutableLiveData<FirebaseUser?>()
    val currentUser get() = _firebaseUser



    private val eventsChannel= Channel<AuthEvents>()
    val authEventsFlow= eventsChannel.receiveAsFlow()


    fun signIn(email: String, password:String)= viewModelScope.launch {
        when{
            email.isEmpty()->{
                eventsChannel.send(AuthEvents.ErrorCode(1))
            }
            password.isEmpty()->{
                eventsChannel.send(AuthEvents.ErrorCode(2))
            }
            else->{
               actualSignInUser(email, password)
            }
        }

    }

    fun signInWithGoogle(authCredential: AuthCredential)= viewModelScope.launch {
        try {
            val user= repository.signInWithGoogle(authCredential)
            user?.let {
                _firebaseUser.postValue(it)
                eventsChannel.send(AuthEvents.Message("Login Success"))
            }
        } catch (e: ApiException){
            eventsChannel.send(AuthEvents.Error(e.message.toString()))
        }
    }

    fun signInWithFacebook(authCredential: AuthCredential)= viewModelScope.launch {
        try {
            val user= repository.signInWithFacebook(authCredential)
            user?.let {
                _firebaseUser.postValue(it)
                eventsChannel.send(AuthEvents.Message("Login Success"))
            }
        }catch (e: ApiException){
            eventsChannel.send(AuthEvents.Error(e.message.toString()))
        }
    }


    private fun actualSignInUser(email: String, password: String) = viewModelScope.launch {
        try {
            val user= repository.signInWithEmailPassword(email, password)
            user?.let {
                _firebaseUser.postValue(it)
                eventsChannel.send(AuthEvents.Message("Login Success"))
            }
        } catch (e: Exception){
            eventsChannel.send(AuthEvents.Error(e.message.toString()))
        }
    }

}