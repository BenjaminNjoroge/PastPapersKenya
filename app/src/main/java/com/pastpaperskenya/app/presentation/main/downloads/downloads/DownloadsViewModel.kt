package com.pastpaperskenya.app.presentation.main.downloads.downloads

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pastpaperskenya.app.business.model.Download
import com.pastpaperskenya.app.business.repository.main.downloads.DownloadsRepository
import com.pastpaperskenya.app.business.util.sealed.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadsViewModel @Inject constructor(
    private val downloadsRepository: DownloadsRepository
): ViewModel() {

//    private var _downloads = MutableLiveData<Resource<List<Download>>>()
//    val downloads: LiveData<Resource<List<Download>>> = _downloads


//    init {
//
//        val firebaseAuth= FirebaseAuth.getInstance()
//        val user= firebaseAuth.currentUser?.uid
//
//        viewModelScope.launch {
//            val id= user?.let { downloadsRepository.getUserDetails(it) }
//            fetchDownloads(id?.userServerId?.toInt())
//        }
//    }
//
//    private fun fetchDownloads(id: Int?)= viewModelScope.launch {
//        downloadsRepository.getDownloads(id).collect{
//            _downloads.postValue(it)
//        }
//    }
}