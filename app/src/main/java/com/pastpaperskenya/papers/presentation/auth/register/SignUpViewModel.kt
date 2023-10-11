package com.pastpaperskenya.papers.presentation.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.pastpaperskenya.papers.business.model.user.Customer
import com.pastpaperskenya.papers.business.model.user.UserDetails
import com.pastpaperskenya.papers.business.util.AuthEvents
import com.pastpaperskenya.papers.business.repository.auth.FirebaseAuthRepository
import com.pastpaperskenya.papers.business.repository.main.user.ServerCrudRepository
import com.pastpaperskenya.papers.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.papers.business.usecases.FirestoreUserService
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
class SignUpViewModel @Inject constructor
    (private val repository: FirebaseAuthRepository,
     private val firestoreUserService: FirestoreUserService,
     private val localUserService: LocalUserService,
     private val datastore: DataStoreRepository,
     private val serverRepository: ServerCrudRepository
) :ViewModel() {

    private  val TAG = "SignUpViewModel"

    private var _userResponse: MutableLiveData<Response<Customer>> = MutableLiveData()
    val userResponse: LiveData<Response<Customer>> = _userResponse

    private val _localResponse= MutableLiveData<Long>()
    val localResponse: LiveData<Long> = _localResponse


    private var _firebaseUser= MutableLiveData<FirebaseUser?>()
    val firebaseUser get() = _firebaseUser

    private var eventsChannel= Channel<AuthEvents>()
    val authEventsFlow= eventsChannel.receiveAsFlow()


    fun writeToDataStore(key:String ,serverId: String) = viewModelScope.launch {
        datastore.setValue(key, serverId)
    }

    fun fieldsChecker(email: String, firstname: String, lastname: String, password: String, confirmPassword: String)= viewModelScope.launch {
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
            password.isEmpty() ->{
                eventsChannel.send(AuthEvents.ErrorCode(4))
            }
            password!=confirmPassword || confirmPassword.isEmpty()->{
                eventsChannel.send(AuthEvents.ErrorCode(5))
            }

            else->{
                createUserInServer(email, firstname, lastname, password)
            }
        }
    }

    private fun createUserInServer(email: String, firstname: String, lastname: String, password: String){
        viewModelScope.launch {
            try {
                val response= serverRepository.createUser(email, firstname, lastname, password)
                _userResponse.value= response
            } catch (e: IOException){
                eventsChannel.send(AuthEvents.Message("Unable to create user in server. Please check Internet bundles"))
            }

        }
    }

    fun registerAndSaveToFirestore(email: String, phone: String, firstname: String, lastname: String, country: String, county: String, password: String, userServerId: Int?) = viewModelScope.launch {

        try {
            val user= repository.signUpWithEmailPassword(email, password)
            user?.let{
                _firebaseUser.postValue(it)
                eventsChannel.send(AuthEvents.Message("User Registered Successfully"))

                val userIds= user.uid

                firestoreUserService.saveUserDetails(UserDetails(userIds, email, phone, firstname, lastname, country, county, userServerId!!))

                eventsChannel.send(AuthEvents.Message("Success \n User saved to database"))

            }
        }catch (e: Exception){
            eventsChannel.send(AuthEvents.Error(e.message.toString()))
        }
    }

    fun insertUserDetails(user: UserDetails){
        viewModelScope.launch(Dispatchers.IO) {
            _localResponse.postValue(localUserService.insertUserToDatabase(user))
        }
    }

}