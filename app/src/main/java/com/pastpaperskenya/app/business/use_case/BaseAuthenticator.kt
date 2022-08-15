package com.pastpaperskenya.app.business.use_case

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser

interface BaseAuthenticator {

    suspend fun registerUser(email: String, password: String): FirebaseUser?

    suspend fun loginUser(email: String, password: String):FirebaseUser?

    suspend fun sendResetPassword(email: String)

    fun logout(): FirebaseUser?

    fun getCurrentUser(): FirebaseUser?

    suspend fun signInWithGoogle(credential: AuthCredential): FirebaseUser?

    suspend fun signInWithFacebook(credential: AuthCredential):FirebaseUser?
}