package com.pastpaperskenya.papers.business.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.pastpaperskenya.papers.business.util.sealed.NetworkResult
import kotlinx.coroutines.Dispatchers


 fun <T, A> networkBoundResource (
     databaseQuery : () -> LiveData<T>,
     networkFetch : suspend ()-> NetworkResult<A>,
     saveNetworkData: suspend (A) -> Unit
): LiveData<NetworkResult<T & Any>> =

    liveData(Dispatchers.IO){
        emit(NetworkResult.loading())

        val source= databaseQuery.invoke().map { NetworkResult.success(it) }
        emitSource(source)

        val response= networkFetch.invoke()
        if (response.status == NetworkResult.Status.SUCCESS){
            saveNetworkData(response.data!!)
        } else if(response.status == NetworkResult.Status.ERROR){
            emit(NetworkResult.error(response.message!!))
            emitSource(source)
        }
    }
