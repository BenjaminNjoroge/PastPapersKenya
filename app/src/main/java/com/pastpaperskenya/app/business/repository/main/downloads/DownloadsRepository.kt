package com.pastpaperskenya.app.business.repository.main.downloads

import com.pastpaperskenya.app.business.services.main.RetrofitService
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DownloadsRepository@Inject constructor(private val retrofitService: RetrofitService){

    suspend fun getDownloads(id: Int) = flow{
        emit(NetworkResult.Loading(true))

        val downloadsResponse= retrofitService.getDownloads(id)
        emit(NetworkResult.Success(downloadsResponse))
    }.catch { e->
        emit(NetworkResult.Error(e.message ?: "Unkwown error"))
    }
}