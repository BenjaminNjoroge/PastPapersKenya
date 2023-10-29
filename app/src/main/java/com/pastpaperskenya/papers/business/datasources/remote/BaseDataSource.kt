package com.pastpaperskenya.papers.business.datasources.remote

import com.pastpaperskenya.papers.business.util.sealed.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): NetworkResult<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return NetworkResult.success(body)
            }
            return error1(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error1(e.message ?: e.toString())
        }
    }


    private fun <T> error1(message: String): NetworkResult<T> {
        Timber.d(message)
        return NetworkResult.error("Network call has failed for a following reason: $message")
    }

    suspend fun <T>safeApiCall(api: suspend () -> Response<T>): NetworkResult<T>{

        return withContext(Dispatchers.IO){
            try {
                val response: Response<T> = api()
                if (response.isSuccessful){
                    NetworkResult.success(data = response.body()!!)
                } else{
                    NetworkResult.error(message = "Something went wrong "+response.toString(), null)
                }
            } catch (e: HttpException){
                //http exception
                NetworkResult.error(message = "Http error "+e.message(), null)
            } catch (e: IOException){
                //no internet
                NetworkResult.error(message = "Please check your network connection", null)
            } catch (e: Exception){
                // of unknown error wrapped in Resource.Error
                NetworkResult.error(message = "Something went wrong  ${e.message}", null)
            }
        }
    }

}