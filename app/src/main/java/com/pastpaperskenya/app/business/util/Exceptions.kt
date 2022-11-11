package com.pastpaperskenya.app.business.util

import com.pastpaperskenya.app.business.util.sealed.ResourceOne
import com.pastpaperskenya.app.business.model.user.ApiResponse
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import timber.log.Timber

internal fun <T> parseException(e: Exception): ResourceOne<T> {
    Timber.e(e)
    val genericErrorMessage = "Something went wrong. Please try again."

    return when (e) {
        is HttpException -> {
            e.response()?.errorBody()?.string()?.let { errorString ->
                try {
                    val apiResponse = appJsonConfig.decodeFromString(ApiResponse.serializer(), errorString)
                    Timber.e("${e.response()?.code()}, $errorString")
                    ResourceOne.Failure<T>(apiResponse)
                } catch (e: SerializationException) {
                    ResourceOne.Failure<T>(ApiResponse(errorString))
                }
            } ?: ResourceOne.Failure(ApiResponse(genericErrorMessage))
        }
        else -> {
            ResourceOne.Failure(ApiResponse(e.message ?: genericErrorMessage))
        }
    }
}