package com.pastpaperskenya.app.business.util

import com.pastpaperskenya.app.business.util.sealed.ResourceOne


@Suppress("TooGenericExceptionCaught")
internal suspend fun <T> call(block: suspend () -> T): ResourceOne<T> {
    return try {
        ResourceOne.Success(block())
    } catch (e: Exception) {
        return parseException(e)
    }
}