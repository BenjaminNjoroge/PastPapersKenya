package com.pastpaperskenya.papers.presentation.auth.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.*
import com.pastpaperskenya.papers.business.model.user.Customer
import com.pastpaperskenya.papers.business.model.user.UserDetails
import com.pastpaperskenya.papers.business.util.AuthEvents
import com.pastpaperskenya.papers.business.repository.auth.FirebaseAuthRepository
import com.pastpaperskenya.papers.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.papers.business.repository.main.user.ServerCrudRepository
import com.pastpaperskenya.papers.business.usecases.LocalUserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor
    (
    private val repository: FirebaseAuthRepository,
    private val serverRepository: ServerCrudRepository,
    private val localUserService: LocalUserService,
    private val datastore: DataStoreRepository,

    ) : ViewModel() {

    private val TAG = "SignInViewModel"

    private val _localResponse= MutableLiveData<Long>()
    val localResponse: LiveData<Long> = _localResponse

    private val _firestoreUserProfile = MutableLiveData<UserDetails>()
    val firestoreUserProfile: LiveData<UserDetails> = _firestoreUserProfile

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
                    eventsChannel.send(AuthEvents.Error("Supplied Email address does not exits in our server. please register first"))
                } else {
                    _userResponse.value = response
                }
            } catch (error: IOException) {
                Log.d(TAG, "checkUserExistsInServer: ")
                eventsChannel.send(AuthEvents.Error("Please check Internet bundles / connection"))
            }
        }
    }


    fun writeToDataStore(key:String ,serverId: String) = viewModelScope.launch {
        datastore.clear()
        datastore.setValue(key, serverId)
    }

    fun actualSignInUser(email: String, password: String, userServerId: Int) = viewModelScope.launch {
        try {
            val user = repository.signInWithEmailPassword(email, password)

            user?.let {

                try {
                    val response= serverRepository.updatePassword(userServerId, password)
                    if (response.isSuccessful) {
                        _firebaseUser.postValue(it)
                        eventsChannel.send(AuthEvents.Message("Login Success"))
                    } else{
                        eventsChannel.send(AuthEvents.Error(response.message()))
                    }
                } catch (e: IOException){
                    eventsChannel.send(AuthEvents.Error("$e Opps.. an error occured"))
                }
            }

        } catch (e: Exception) {
            eventsChannel.send(AuthEvents.Error(e.message.toString()))
        }
    }

    fun actualGoogleSignIn(credential:AuthCredential) = viewModelScope.launch {
        try {
            val user = repository.signInWithGoogle(credential)

            user.let {
                try {
                    if (it.isSuccessful) {
                        _firebaseUser.postValue(it.result.user)
                        eventsChannel.send(AuthEvents.Message("Login Success"))
                    } else{
                        eventsChannel.send(AuthEvents.Error(it.exception?.localizedMessage.toString()))
                    }
                } catch (e: IOException){
                    eventsChannel.send(AuthEvents.Error("$e Opps.. an error occured"))
                }
            }

        } catch (e: Exception) {
            eventsChannel.send(AuthEvents.Error(e.message.toString()))
        }
    }


    fun insertUserDetails(user: UserDetails){
        viewModelScope.launch(Dispatchers.IO) {
            _localResponse.postValue(localUserService.insertUserToDatabase(user))
        }
    }
}