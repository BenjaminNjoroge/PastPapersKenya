package com.pastpaperskenya.app.business.datasources.remote

import com.pastpaperskenya.app.business.util.sealed.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

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


    private fun <T> error1(message: String): Resource<T> {
        Timber.d(message)
        return Resource.error("Network call has failed for a following reason: $message")
    }

    suspend fun <T>safeApiCall(api: suspend () -> Response<T>): Resource<T>{

        return withContext(Dispatchers.IO){
            try {
                val response: Response<T> = api()
                if (response.isSuccessful){
                    Resource.success(data = response.body()!!)
                } else{
                    Resource.error(message = "Something went wrong "+response.errorBody().toString(), null)
                }
            } catch (e: HttpException){
                //http exception
                Resource.error(message = "Http error "+e.message(), null)
            } catch (e: IOException){
                //no internet
                Resource.error(message = "Please check your network connection", null)
            } catch (e: Exception){
                // of unknown error wrapped in Resource.Error
                  Resource.error(message = "Something went wrong", null)
            }
        }
    }

}