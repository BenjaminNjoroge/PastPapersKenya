package com.pastpaperskenya.papers.business.datasources.remote.services.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthenticatorImpl: BaseAuthenticator {

    private  val TAG = "Authenticator"
    override suspend fun registerUser(email: String, password: String): FirebaseUser? {
        Firebase.auth.createUserWithEmailAndPassword(email, password).await()
        return Firebase.auth.currentUser
    }

    override suspend fun loginUser(email: String, password: String): FirebaseUser? {
        Firebase.auth.signInWithEmailAndPassword(email, password).await()
        return Firebase.auth.currentUser
    }

    override suspend fun sendResetPassword(email: String) {
        Firebase.auth.sendPasswordResetEmail(email).await()
    }

    override fun logout(): FirebaseUser? {
        Firebase.auth.signOut()
        return Firebase.auth.currentUser
    }

    override fun getCurrentUser(): FirebaseUser? {
        return Firebase.auth.currentUser
    }

    override suspend fun signInWithGoogle(credential: AuthCredential): Task<AuthResult> {
        return Firebase.auth.signInWithCredential(credential)
    }

}