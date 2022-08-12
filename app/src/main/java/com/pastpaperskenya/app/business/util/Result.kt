package com.pastpaperskenya.app.business.util

sealed class Result{
    object Loading: Result()
    data class Error(val message: String): Result()
    data class Success(val message: String?= null, val data:Any?= null): Result()
}
