package com.pastpaperskenya.papers.business.repository.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.pastpaperskenya.papers.business.datasources.remote.services.auth.BaseAuthenticator
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val authenticator: BaseAuthenticator
) : FirebaseAuthRepository {

    override suspend fun signInWithEmailPassword(email: String, password: String): FirebaseUser? {
        return authenticator.loginUser(email, password)
    }

    override suspend fun signUpWithEmailPassword(email: String, password: String): FirebaseUser? {
        return authenticator.registerUser(email, password)
    }

    override fun signOut(): FirebaseUser? {
        return authenticator.logout()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return authenticator.getCurrentUser()
    }

    override suspend fun sendResetPassword(email: String): Boolean {
        authenticator.sendResetPassword(email)
        return true
    }

    override suspend fun signInWithGoogle(credential: AuthCredential): Task<AuthResult> {
        return authenticator.signInWithGoogle(credential)
    }

}