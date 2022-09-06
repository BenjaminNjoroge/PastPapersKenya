package com.pastpaperskenya.app.business.repository.auth

import com.google.firebase.auth.FirebaseUser
import com.pastpaperskenya.app.business.model.auth.Customer
import retrofit2.Response


interface FirebaseRepository {

    suspend fun signInWithEmailPassword(email:String , password:String): FirebaseUser?

    suspend fun signUpWithEmailPassword(email: String , password: String): FirebaseUser?

    fun signOut() : FirebaseUser?

    fun getCurrentUser() : FirebaseUser?

    suspend fun sendResetPassword(email : String) : Boolean

    suspend fun createUser(email: String, firstname: String, lastname: String, password: String): Response<Customer>

    suspend fun getUser(email: String): Response<List<Customer>>

}