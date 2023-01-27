package com.pastpaperskenya.papers.business.repository.main.user

import com.pastpaperskenya.papers.business.datasources.remote.services.main.RetrofitApiService
import com.pastpaperskenya.papers.business.model.user.Customer
import com.pastpaperskenya.papers.business.model.user.CustomerUpdate
import retrofit2.Response
import javax.inject.Inject

class ServerCrudRepositoryImpl @Inject constructor(
    private val retrofitService: RetrofitApiService
): ServerCrudRepository {

    override suspend fun createUser(email: String, firstname: String, lastname: String, password: String): Response<Customer> {
        return retrofitService.createUser(email, firstname, lastname, password)
    }

    override suspend fun getUser(email: String): Response<List<Customer>> {
        return retrofitService.getUser(email)
    }

    override suspend fun updateUser(id: Int, customer: CustomerUpdate): Response<CustomerUpdate> {
        return retrofitService.updateUser(id, customer)
    }

    override suspend fun updatePassword(id: Int, password: String): Response<Customer> {
        return retrofitService.updateUserPassword(id, password)
    }


}