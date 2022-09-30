package com.pastpaperskenya.app.business.datasources.remote.services.payment

import com.pastpaperskenya.app.business.model.auth.ApiResponse
import com.pastpaperskenya.app.business.model.lipanampesa.RequestMpesaDto
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentsService {
    @POST("mpesa/request")
    suspend fun makeRequest(
        @Body lnmoRequest: RequestMpesaDto
    ): ApiResponse
}