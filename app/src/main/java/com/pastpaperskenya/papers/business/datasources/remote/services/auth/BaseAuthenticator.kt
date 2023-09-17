package com.pastpaperskenya.papers.business.datasources.remote.services.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface BaseAuthenticator {

    suspend fun registerUser(email: String, password: String): FirebaseUser?

    suspend fun loginUser(email: String, password: String):FirebaseUser?

    suspend fun sendResetPassword(email: String)

    fun logout(): FirebaseUser?

    fun getCurrentUser(): FirebaseUser?

    suspend fun signInWithGoogle(credential: AuthCredential) : Task<AuthResult>

}