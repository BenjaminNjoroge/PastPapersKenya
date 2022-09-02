package com.pastpaperskenya.app.business.util.sealed

sealed class NetworkResult<T>{
    data class Loading<T>(val loading: Boolean): NetworkResult<T>()
    data class Error<T>(val error: String): NetworkResult<T>()
    data class Success<T>(val data: T): NetworkResult<T>()
}
