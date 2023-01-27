package com.pastpaperskenya.papers.presentation.main.downloads.downloads

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pastpaperskenya.papers.business.model.download.Download
import com.pastpaperskenya.papers.business.util.AuthEvents
import com.pastpaperskenya.papers.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.papers.business.repository.main.downloads.DownloadsRepository
import com.pastpaperskenya.papers.business.util.Constants
import com.pastpaperskenya.papers.business.util.convertIntoNumeric
import com.pastpaperskenya.papers.business.util.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadsFragmentViewModel @Inject constructor(
    private val downloadsRepository: DownloadsRepository,
    private val datastore: DataStoreRepository
): ViewModel() {

    private var _downloads = MutableLiveData<NetworkResult<List<Download>>>()
    val downloads: LiveData<NetworkResult<List<Download>>> = _downloads

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