package com.pastpaperskenya.app.business.repository.main.downloads

import com.pastpaperskenya.app.business.datasources.remote.services.main.RetrofitService
import com.pastpaperskenya.app.business.util.sealed.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DownloadsRepository @Inject constructor(
    private val retrofitService: RetrofitService,
){

        suspend fun getDownloads(id: Int?) = flow{
        emit(Resource.loading())

        val downloadsResponse= retrofitService.getDownloads(id)
        emit(Resource.success(downloadsResponse))
    }.catch { e->
        emit(Resource.error(e.message ?: "Unkwown error"))
    }.flowOn(Dispatchers.IO)


}