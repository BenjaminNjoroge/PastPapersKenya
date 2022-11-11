package com.pastpaperskenya.app.presentation.main.profile.editprofile

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import com.pastpaperskenya.app.business.model.user.Customer
import com.pastpaperskenya.app.business.model.user.UserDetails
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.business.repository.auth.FirebaseRepository
import com.pastpaperskenya.app.business.repository.auth.ServerCrudRepository
import com.pastpaperskenya.app.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.app.business.repository.main.profile.EditProfileRepository
import com.pastpaperskenya.app.business.usecases.FirestoreUserService
import com.pastpaperskenya.app.business.usecases.LocalUserService
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.convertIntoNumeric
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val editProfileRepository: EditProfileRepository,
    private val datastore:DataStoreRepository,
) : ViewModel() {

    private val _loading= MutableLiveData(false)
    val loading get() = _loading

    private val _userProfile= MutableLiveData<UserDetails>()
    val userProfile : LiveData<UserDetails> = _userProfile


    private var _firebaseUser= MutableLiveData<FirebaseUser?>()
    val firebaseUser get() = _firebaseUser

    private var _eventsChannel= Channel<AuthEvents>()
    val authEventsChannel= _eventsChannel.receiveAsFlow()

    private var _userServerId= MutableLiveData<String>()
    val userServerId: LiveData<String> = _userServerId

    private var _updateServerDetails= MutableLiveData<NetworkResult<Customer>>()
    val updateServerDetails: LiveData<NetworkResult<Customer>> = _updateServerDetails

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val id= datastore.getValue(Constants.USER_SERVER_ID)
            getUserDetails(convertIntoNumeric(id!!))
        }
    }

    fun updateFirestoreDetails(userId: String, phone: String, firstname: String, lastname :String, country:String, county: String)= viewModelScope.launch {
        editProfileRepository.updateUserToFirebase(userId, phone, firstname, lastname, country, county)
    }

    fun updateLocalDetails(phone: String, firstname: String, lastname :String, country:String, county: String, userServerId:Int)= viewModelScope.launch {
        editProfileRepository.updateUserToDatabase( phone, firstname, lastname, country, county, userServerId)
    }


    fun fieldsChecker(userServerId: Int, phone: String, firstname: String, lastname: String, country: String, county: String)= viewModelScope.launch{
        when{
            firstname.isEmpty()->{
                _eventsChannel.send(AuthEvents.ErrorCode(1))
            }
            lastname.isEmpty()->{
                _eventsChannel.send(AuthEvents.ErrorCode(2))
            }
            lastname.isEmpty()->{
                _eventsChannel.send(AuthEvents.ErrorCode(3))
            }

            else->{
               updateServerDetails(userServerId, phone, firstname, lastname, country, county)
        }
    }
    }

    private suspend fun updateServerDetails(userServerId: Int, phone: String, firstname: String, lastname: String, country: String, county: String)= viewModelScope.launch {
        editProfileRepository.updateUserToServer(userServerId, firstname, lastname, phone, country, county).collect{
           _updateServerDetails.value= it
            delay(2000)
            _eventsChannel.send(AuthEvents.Message("Updated successfully"))
        }
    }

    private suspend fun getUserDetails(userServerId: Int){
        editProfileRepository.getUserDetails(userServerId)?.collect{
            _userProfile.postValue(it)
        }
    }

    fun logout()= viewModelScope.launch {
        try {
            val user= firebaseRepository.signOut()
            user?.let {
                _eventsChannel.send(AuthEvents.Message("logout failure"))
            }?: _eventsChannel.send(AuthEvents.Message("Logout Success"))

            getCurrentUser()

        } catch (e: Exception){
            _eventsChannel.send(AuthEvents.Error(e.message.toString()))
        }
    }

    private fun getCurrentUser()= viewModelScope.launch {
        val user= firebaseRepository.getCurrentUser()
        _firebaseUser.postValue(user)
    }


}