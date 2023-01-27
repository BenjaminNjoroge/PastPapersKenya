package com.pastpaperskenya.papers.business.repository.main.user

import com.pastpaperskenya.papers.business.model.user.Customer
import com.pastpaperskenya.papers.business.model.user.CustomerUpdate
import retrofit2.Response

interface ServerCrudRepository {

    suspend fun createUser(email: String, firstname: String, lastname: String, password: String): Response<Customer>

    suspend fun getUser(email: String): Response<List<Customer>>

    suspend fun updateUser(id: Int, customer: CustomerUpdate): Response<CustomerUpdate>

    suspend fun updatePassword(id: Int, password: String): Response<Customer>

}