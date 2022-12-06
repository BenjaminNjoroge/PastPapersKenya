package com.pastpaperskenya.app.business.util.sealed

data class NetworkResult<out T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): NetworkResult<T> {
            return NetworkResult(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T? = null): NetworkResult<T> {
            return NetworkResult(Status.ERROR, data, message)
        }

        fun <T> loading(data: T? = null): NetworkResult<T> {
            return NetworkResult(Status.LOADING, data, null)
        }
    }
}
