package com.pastpaperskenya.app.business.repository.main.downloads

import com.pastpaperskenya.app.business.datasources.remote.services.main.RetrofitApiService
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DownloadsRepository @Inject constructor(
    private val retrofitApiService: RetrofitApiService,
){

        suspend fun getDownloads(id: Int?) = flow{
        emit(NetworkResult.loading())

        val downloadsResponse= retrofitApiService.getDownloads(id)
        emit(NetworkResult.success(downloadsResponse))
    }.catch { e->
        emit(NetworkResult.error(e.message ?: "Unkwown error"))
    }.flowOn(Dispatchers.IO)


}