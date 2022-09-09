package com.pastpaperskenya.app.presentation.main.downloads.downloads

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pastpaperskenya.app.business.model.Download
import com.pastpaperskenya.app.business.model.auth.UserDetails
import com.pastpaperskenya.app.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.app.business.repository.main.downloads.DownloadsRepository
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadsViewModel @Inject constructor(
    private val downloadsRepository: DownloadsRepository
): ViewModel() {

    private var _downloads = MutableLiveData<NetworkResult<List<Download>>>()
    val downloads: LiveData<NetworkResult<List<Download>>> = _downloads


    init {

        val firebaseAuth= FirebaseAuth.getInstance()
        val user= firebaseAuth.currentUser?.uid

        viewModelScope.launch {
            val id= downloadsRepository.getUserDetails(user!!)
            fetchDownloads(id?.userServerId?.toInt())
        }
    }

    private fun fetchDownloads(id: Int?)= viewModelScope.launch {
        downloadsRepository.getDownloads(id).collect{
            _downloads.postValue(it)
        }
    }
}