package com.pastpaperskenya.app.presentation.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.*
import com.pastpaperskenya.app.business.model.auth.Customer
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.business.repository.auth.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor
    (private val repository: FirebaseRepository,): ViewModel(){

    private  val TAG = "SignInViewModel"

    private var _userResponse: MutableLiveData<Response<List<Customer>>> = MutableLiveData()
    val userResponse: LiveData<Response<List<Customer>>> = _userResponse

    private var _firebaseUser= MutableLiveData<FirebaseUser?>()
    val currentUser get() = _firebaseUser

    private val eventsChannel= Channel<AuthEvents>()
    val authEventsFlow= eventsChannel.receiveAsFlow()


    fun fieldsChecker(email: String, password:String)= viewModelScope.launch {
        when{
            email.isEmpty()->{
                eventsChannel.send(AuthEvents.ErrorCode(1))
            }
            password.isEmpty()->{
                eventsChannel.send(AuthEvents.ErrorCode(2))
            }
            else->{
               checkUserExistsInServer(email)
            }
        }
    }

    private fun checkUserExistsInServer(email: String){
        viewModelScope.launch {
            val response = repository.getUser(email)
            if(!response.isSuccessful) {
                eventsChannel.send(AuthEvents.Message("Supplied Email address does not exits in our server. please register first"))
            }else{
                _userResponse.value = response
            }

        }
    }

     fun actualSignInUser(email: String, password: String) = viewModelScope.launch {
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