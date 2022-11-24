package com.pastpaperskenya.app.presentation.auth.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.*
import com.pastpaperskenya.app.business.model.user.Customer
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.business.repository.auth.FirebaseRepository
import com.pastpaperskenya.app.business.repository.auth.ServerCrudRepository
import com.pastpaperskenya.app.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.app.business.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor
    (
    private val repository: FirebaseRepository,
    private val serverRepository: ServerCrudRepository,
    private val datastore: DataStoreRepository

) : ViewModel() {

    private val TAG = "SignInViewModel"

    private var _userResponse: MutableLiveData<Response<List<Customer>>> = MutableLiveData()
    val userResponse: LiveData<Response<List<Customer>>> = _userResponse

    private var _firebaseUser = MutableLiveData<FirebaseUser?>()
    val currentUser get() = _firebaseUser

    private val eventsChannel = Channel<AuthEvents>()
    val authEventsFlow = eventsChannel.receiveAsFlow()


    fun fieldsChecker(email: String, password: String) = viewModelScope.launch {
        when {
            email.isEmpty() -> {
                eventsChannel.send(AuthEvents.ErrorCode(1))
            }
            password.isEmpty() -> {
                eventsChannel.send(AuthEvents.ErrorCode(2))
            }
            else -> {
                checkUserExistsInServer(email)
            }
        }
    }

    private fun checkUserExistsInServer(email: String) {
        viewModelScope.launch {
            try {
                val response = serverRepository.getUser(email)
                if (!response.isSuccessful) {
                    eventsChannel.send(AuthEvents.Message("Supplied Email address does not exits in our server. please register first"))
                } else {
                    _userResponse.value = response
                }
            } catch (error: IOException) {
                Log.d(TAG, "checkUserExistsInServer: ")
                eventsChannel.send(AuthEvents.Message("Please check Internet bundles / connection"))
            }


        }
    }

    fun actualSignInUser(email: String, password: String) = viewModelScope.launch {
        try {
            val user = repository.signInWithEmailPassword(email, password)
            if(!datastore.getValue(Constants.RESET_PASSWORD).isNullOrEmpty()){
                val value= datastore.getValue(Constants.KEY_PASSWORD)
               // val response= serverRepository.updateUser()
            } else {
                user?.let {
                    _firebaseUser.postValue(it)
                    eventsChannel.send(AuthEvents.Message("Login Success"))
                }
            }
        } catch (e: Exception) {
            eventsChannel.send(AuthEvents.Error(e.message.toString()))
        }
    }


}