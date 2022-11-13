package com.pastpaperskenya.app.business.repository.auth

import com.pastpaperskenya.app.business.datasources.remote.services.main.RetrofitService
import com.pastpaperskenya.app.business.model.user.Customer
import com.pastpaperskenya.app.business.util.sealed.Resource
import retrofit2.Response
import javax.inject.Inject

class ServerCrudRepositoryImpl @Inject constructor(
    private val retrofitService: RetrofitService
): ServerCrudRepository{

    override suspend fun createUser(email: String, firstname: String, lastname: String, password: String): Response<Customer> {
        return retrofitService.createUser(email, firstname, lastname, password)
    }

    override suspend fun getUser(email: String): Response<List<Customer>> {
        return retrofitService.getUser(email)
    }


}