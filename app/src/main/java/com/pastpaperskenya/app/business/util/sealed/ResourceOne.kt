package com.pastpaperskenya.app.business.util.sealed

import com.pastpaperskenya.app.business.model.user.ApiResponse

sealed class ResourceOne<out T> {
    data class Success<out T>(val data: T) : ResourceOne<T>()
    data class Failure<out T>(val response: ApiResponse) : ResourceOne<T>()
}