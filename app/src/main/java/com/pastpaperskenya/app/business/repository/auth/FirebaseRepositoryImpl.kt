package com.pastpaperskenya.app.business.repository.auth

import androidx.lifecycle.LiveData
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pastpaperskenya.app.business.model.Customer
import com.pastpaperskenya.app.business.use_case.BaseAuthenticator
import com.pastpaperskenya.app.business.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(private val authenticator: BaseAuthenticator):FirebaseRepository {

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


}