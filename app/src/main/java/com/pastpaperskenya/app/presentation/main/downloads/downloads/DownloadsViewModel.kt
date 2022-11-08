package com.pastpaperskenya.app.presentation.main.downloads.downloads

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pastpaperskenya.app.business.model.download.Download
import com.pastpaperskenya.app.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.app.business.repository.main.downloads.DownloadsRepository
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.convertIntoNumeric
import com.pastpaperskenya.app.business.util.sealed.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadsViewModel @Inject constructor(
    private val downloadsRepository: DownloadsRepository,
    private val datastore: DataStoreRepository
): ViewModel() {

    private var _downloads = MutableLiveData<Resource<List<Download>>>()
    val downloads: LiveData<Resource<List<Download>>> = _downloads


    init {

        viewModelScope.launch {
            val id= datastore.getValue(Constants.USER_SERVER_ID)
            fetchDownloads((convertIntoNumeric(id!!)))
        }
    }

    private fun fetchDownloads(id: Int)= viewModelScope.launch {
        downloadsRepository.getDownloads(id).collect{
            _downloads.postValue(it)
        }
    }
}