package com.pastpaperskenya.app.business.repository.auth

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser


interface FirebaseRepository {

    suspend fun signInWithEmailPassword(email:String , password:String): FirebaseUser?

    suspend fun signUpWithEmailPassword(email: String , password: String): FirebaseUser?

    fun signOut() : FirebaseUser?

    fun getCurrentUser() : FirebaseUser?

    suspend fun sendResetPassword(email : String) : Boolean

    suspend fun signInWithGoogle(authCredential: AuthCredential): FirebaseUser?

    suspend fun signInWithFacebook(authCredential: AuthCredential):FirebaseUser?

}