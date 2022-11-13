package com.pastpaperskenya.app.business.repository.auth

import com.pastpaperskenya.app.business.model.user.Customer
import retrofit2.Response

interface ServerCrudRepository {

    suspend fun createUser(email: String, firstname: String, lastname: String, password: String): Response<Customer>

    suspend fun getUser(email: String): Response<List<Customer>>

}