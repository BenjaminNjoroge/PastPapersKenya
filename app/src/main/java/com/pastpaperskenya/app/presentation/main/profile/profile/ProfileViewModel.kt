package com.pastpaperskenya.app.presentation.main.profile.profile

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.pastpaperskenya.app.business.model.auth.UserDetails
import com.pastpaperskenya.app.business.repository.main.profile.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) : ViewModel(){

    private var _isLoading= MutableLiveData(false)
    val loading get() = _isLoading

    private val _userProfile = MutableLiveData<UserDetails>()
    val userProfile: LiveData<UserDetails> = _userProfile

    init {
        val firebaseAuth= FirebaseAuth.getInstance()
        val user= firebaseAuth.currentUser?.uid
        viewModelScope.launch {

            _isLoading.postValue(true)
            _userProfile.value= profileRepository.getUserDetails(user!!)
            _isLoading.postValue(false)
        }
    }



}