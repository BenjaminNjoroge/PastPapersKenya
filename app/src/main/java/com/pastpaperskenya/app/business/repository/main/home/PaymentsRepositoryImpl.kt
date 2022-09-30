package com.pastpaperskenya.app.business.repository.main.home

import com.pastpaperskenya.app.business.model.auth.ApiResponse
import com.pastpaperskenya.app.business.util.sealed.ResourceOne
import com.pastpaperskenya.app.business.model.lipanampesa.RequestMpesaDto
import com.pastpaperskenya.app.business.datasources.remote.services.payment.PaymentsService
import com.pastpaperskenya.app.business.util.call
import com.pastpaperskenya.app.business.util.sanitizePhoneNumber
import javax.inject.Inject

class PaymentsRepositoryImpl @Inject constructor
    (private val paymentsService: PaymentsService) : PaymentsRepository {


    override suspend fun makeLnmoRequest(
        amount: Int,
        phone: String,
        customerId: String,
        accountRef: String
    ): ResourceOne<ApiResponse> {
        return call{
            paymentsService.makeRequest(RequestMpesaDto(amount.toString(), sanitizePhoneNumber(phone), customerId, accountRef))
        }
    }

}