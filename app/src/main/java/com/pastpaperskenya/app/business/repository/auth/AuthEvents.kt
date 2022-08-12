package com.pastpaperskenya.app.business.repository.auth

sealed class AuthEvents{
    data class Message(val message: String): AuthEvents()
    data class Error(val message: String): AuthEvents()
    data class ErrorCode(val code:Int): AuthEvents()
}
