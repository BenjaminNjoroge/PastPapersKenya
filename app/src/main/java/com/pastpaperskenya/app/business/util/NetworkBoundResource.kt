package com.pastpaperskenya.app.business.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.pastpaperskenya.app.business.util.sealed.Resource
import kotlinx.coroutines.Dispatchers


 fun <T, A> networkBoundResource (
     databaseQuery : () -> LiveData<T>,
     networkFetch : suspend ()-> Resource<A>,
     saveNetworkData: suspend (A) -> Unit
): LiveData<Resource<T>> =

    liveData(Dispatchers.IO){
        emit(Resource.loading())

        val source= databaseQuery.invoke().map { Resource.success(it) }
        emitSource(source)

        val response= networkFetch.invoke()
        if (response.status == Resource.Status.SUCCESS){
            saveNetworkData(response.data!!)
        } else if(response.status == Resource.Status.ERROR){
            emit(Resource.error(response.message!!))
            emitSource(source)
        }
    }




