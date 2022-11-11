package com.pastpaperskenya.app.business.datasources.remote

import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import com.pastpaperskenya.app.business.util.sealed.Resource
import retrofit2.Response
import timber.log.Timber

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.success(body)
            }
            return error1(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error1(e.message ?: e.toString())
        }
    }

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body)
                }
            }
            return error2("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error2(e.message ?: e.toString())
        }
    }

    private fun <T> error1(message: String): Resource<T> {
        Timber.d(message)
        return Resource.error("Network call has failed for a following reason: $message")
    }


    private fun <T> error2(message: String): NetworkResult<T> {
        Timber.d(message)
        return NetworkResult.Error("Network call has failed for a following reason: $message")
    }


}