package com.pastpaperskenya.app.presentation.main.profile.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class ProfileViewModel : ViewModel(){

    private var _isLoading= MutableLiveData(false)
    val loading get() = _isLoading
    var result= MutableLiveData<FirebaseUser>()


}