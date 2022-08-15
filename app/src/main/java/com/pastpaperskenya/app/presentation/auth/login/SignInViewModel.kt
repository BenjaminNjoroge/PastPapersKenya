package com.pastpaperskenya.app.presentation.auth.login

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
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


    fun firebaseSignInWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                _firebaseUser.postValue(auth.currentUser)
            }else{
                _firebaseUser.value= null
            }
        }
    }

    fun firebaseSignInWithFacebook(idToken: String){
        val credential = FacebookAuthProvider.getCredential(idToken)
        //Log.d(TAG, "firebaseSignInWithFacebook: "+ credential.toString())
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                _firebaseUser.postValue(auth.currentUser)
            }else{
                _firebaseUser.value= null
            }
        }
    }



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



    private fun actualSignInUser(email: String, password: String) = viewModelScope.launch {
        try {
            val user= repository.signInWithEmailPassword(email, password)
            user?.let {
                _firebaseUser.postValue(it)
                eventsChannel.send(AuthEvents.Error("Login Success"))
            }
        } catch (e: Exception){
            eventsChannel.send(AuthEvents.Error(e.message.toString()))
        }
    }

}