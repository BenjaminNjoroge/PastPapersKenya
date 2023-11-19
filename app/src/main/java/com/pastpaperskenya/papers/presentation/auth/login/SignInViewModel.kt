package com.pastpaperskenya.papers.presentation.auth.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.pastpaperskenya.papers.business.model.user.Customer
import com.pastpaperskenya.papers.business.model.user.UserDetails
import com.pastpaperskenya.papers.business.util.AuthEvents
import com.pastpaperskenya.papers.business.repository.auth.FirebaseAuthRepository
import com.pastpaperskenya.papers.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.papers.business.repository.main.user.ServerCrudRepository
import com.pastpaperskenya.papers.business.usecases.FirestoreUserService
import com.pastpaperskenya.papers.business.usecases.LocalUserService
import com.pastpaperskenya.papers.business.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

sealed class UserResult{
    object Loading: UserResult()
    data class Success(val user: UserDetails): UserResult()
    data class Error(val message: String): UserResult()
}
@HiltViewModel
class SignInViewModel @Inject constructor
    (
    private val repository: FirebaseAuthRepository,
    private val serverRepository: ServerCrudRepository,
    private val localUserService: LocalUserService,
    private val datastore: DataStoreRepository,
    private val firestoreUserService: FirestoreUserService,

    ) : ViewModel() {

    private val TAG = "SignInViewModel"

    private val firestore = FirebaseFirestore.getInstance()

    private val _localResponse= MutableLiveData<Long>()
    val localResponse: LiveData<Long> = _localResponse

    private val fetchResultLiveData: MutableLiveData<UserResult> = MutableLiveData()
    private val saveResultLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getFetchResultLiveData(): LiveData<UserResult> = fetchResultLiveData
    fun getSaveResultLiveData(): LiveData<Boolean> = saveResultLiveData

    private var _userResponse: MutableLiveData<Response<List<Customer>>> = MutableLiveData()
    val userResponse: LiveData<Response<List<Customer>>> = _userResponse

    private var _myUserResponse: MutableLiveData<Response<Customer>> = MutableLiveData()
    val myUserResponse: LiveData<Response<Customer>> = _myUserResponse

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

    fun checkIfUserExistsByEmail(email: String, onSuccess: (Boolean)-> Unit) {
        viewModelScope.launch {
            val exists = firestoreUserService.checkIfUserExistsByEmail(email)
            onSuccess(exists)
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


    fun insertUserDetails(user: UserDetails){
        viewModelScope.launch(Dispatchers.IO) {
            _localResponse.postValue(localUserService.insertUserToDatabase(user))
        }
    }

     fun createUserInServer(email: String, firstname: String, lastname: String, password: String){
        viewModelScope.launch {
            try {
                val response= serverRepository.createUser(email, firstname, lastname, password)
                _myUserResponse.value= response
            } catch (e: IOException){
                eventsChannel.send(AuthEvents.Message("Unable to create user in server. Please check Internet bundles"))
            }

        }
    }

    fun saveToFirestore(firebaseId:String, email: String, phone:String, firstname: String, lastname: String, country: String, county: String, password: String, userServerId: Int?)= viewModelScope.launch{

        firestoreUserService.saveUserDetails(UserDetails(firebaseId, email, phone, firstname, lastname, country, county, userServerId))
    }

    fun getFirestoreDetails(email: String) {
        fetchResultLiveData.value= UserResult.Loading

        viewModelScope.launch {
            try {
                //fetch user data
                val firestoreUser= fetchUserFromFirestore(email)
                if (firestoreUser != null){
                    localUserService.insertUserToDatabase(firestoreUser)
                }
                fetchResultLiveData.value = firestoreUser?.let { UserResult.Success(it) }
                saveResultLiveData.postValue(true)

            } catch (e: Exception){
                fetchResultLiveData.value = UserResult.Error(e.message ?: "An error occurred")
                saveResultLiveData.postValue(false)
            }
        }
    }

    private suspend fun fetchUserFromFirestore(email: String): UserDetails? {
       return firestoreUserService.getFirestoreUserDetails(email)
    }
}