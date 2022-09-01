package com.pastpaperskenya.app.business.util.sealedclasses

import com.pastpaperskenya.app.business.model.auth.ApiResponse

sealed class ResourceOne<out T> {
    data class Success<out T>(val data: T) : ResourceOne<T>()
    data class Failure<out T>(val response: ApiResponse) : ResourceOne<T>()
}