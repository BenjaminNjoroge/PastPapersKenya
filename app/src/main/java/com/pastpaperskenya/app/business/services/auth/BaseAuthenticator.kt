package com.pastpaperskenya.app.business.services.auth

import com.google.firebase.auth.FirebaseUser

interface BaseAuthenticator {

    suspend fun registerUser(email: String, password: String): FirebaseUser?

    suspend fun loginUser(email: String, password: String):FirebaseUser?

    suspend fun sendResetPassword(email: String)

    fun logout(): FirebaseUser?

    fun getCurrentUser(): FirebaseUser?

}