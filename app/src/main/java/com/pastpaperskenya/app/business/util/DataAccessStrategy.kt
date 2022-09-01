package com.pastpaperskenya.app.business.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.pastpaperskenya.app.business.util.sealedclasses.Resource
import kotlinx.coroutines.Dispatchers

fun<T, A> performDataAccessStrategy(databaseQuery: () -> LiveData<T>,
                                    networkCall: suspend () -> Resource<A>,
                                    saveCallRequest: suspend (A) -> Unit) : LiveData<Resource<T>> =
    liveData(Dispatchers.IO){
        emit(Resource.loading())
        val source= databaseQuery.invoke().map { Resource.success(it) }
        emitSource(source)

        val responseStatus= networkCall.invoke()
        if (responseStatus.status == Resource.Status.SUCCESS){
            saveCallRequest(responseStatus.data!!)

        } else if(responseStatus.status == Resource.Status.ERROR){
            emit(Resource.error(responseStatus.message!!))
            emitSource(source)
        }
    }