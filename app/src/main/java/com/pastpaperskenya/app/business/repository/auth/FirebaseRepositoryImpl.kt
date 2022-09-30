package com.pastpaperskenya.app.business.repository.auth

import com.google.firebase.auth.FirebaseUser
import com.pastpaperskenya.app.business.model.auth.Customer
import com.pastpaperskenya.app.business.datasources.remote.services.auth.BaseAuthenticator
import com.pastpaperskenya.app.business.datasources.remote.services.main.RetrofitService
import retrofit2.Response
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val authenticator: BaseAuthenticator,
    private val retrofitService: RetrofitService
):FirebaseRepository {

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

    override suspend fun createUser(email: String, firstname: String, lastname: String, password: String): Response<Customer> {
        return retrofitService.createUser(email, firstname, lastname, password)
    }

    override suspend fun getUser(email: String): Response<List<Customer>> {
        return retrofitService.getUser(email)
    }


}