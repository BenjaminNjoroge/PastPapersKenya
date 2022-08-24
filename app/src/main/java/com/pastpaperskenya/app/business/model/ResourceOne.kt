package com.pastpaperskenya.app.business.model

sealed class ResourceOne<out T> {
    data class Success<out T>(val data: T) : ResourceOne<T>()
    data class Failure<out T>(val response: ApiResponse) : ResourceOne<T>()
}