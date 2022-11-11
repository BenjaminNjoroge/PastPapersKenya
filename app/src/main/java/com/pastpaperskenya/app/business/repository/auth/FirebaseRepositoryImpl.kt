package com.pastpaperskenya.app.business.repository.auth

import com.google.firebase.auth.FirebaseUser
import com.pastpaperskenya.app.business.model.user.Customer
import com.pastpaperskenya.app.business.datasources.remote.services.auth.BaseAuthenticator
import com.pastpaperskenya.app.business.datasources.remote.services.main.RetrofitService
import retrofit2.Response
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val authenticator: BaseAuthenticator
) : FirebaseRepository {

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