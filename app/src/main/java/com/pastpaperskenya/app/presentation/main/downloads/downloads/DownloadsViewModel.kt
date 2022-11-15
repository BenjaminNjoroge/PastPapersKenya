package com.pastpaperskenya.app.presentation.main.downloads.downloads

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pastpaperskenya.app.business.model.download.Download
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.app.business.repository.main.downloads.DownloadsRepository
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.convertIntoNumeric
import com.pastpaperskenya.app.business.util.sealed.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DownloadsViewModel @Inject constructor(
    private val downloadsRepository: DownloadsRepository,
    private val datastore: DataStoreRepository
): ViewModel() {

    private var _downloads = MutableLiveData<Resource<List<Download>>>()
    val downloads: LiveData<Resource<List<Download>>> = _downloads

    private var eventsChannel= Channel<AuthEvents>()
    val events= eventsChannel.receiveAsFlow()

    init {

        viewModelScope.launch {
            val id= datastore.getValue(Constants.USER_SERVER_ID)
            try{
                fetchDownloads((convertIntoNumeric(id!!)))
            } catch (e: Exception){
                eventsChannel.send(AuthEvents.Error("Unable to get data $e"))
            }
        }
    }

    private suspend fun fetchDownloads(id: Int)=
        downloadsRepository.getDownloads(id).collect{
            _downloads.postValue(it)
        }

}