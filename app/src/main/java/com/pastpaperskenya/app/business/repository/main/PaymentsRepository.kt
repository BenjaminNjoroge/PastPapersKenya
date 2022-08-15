package com.pastpaperskenya.app.business.repository.main

import com.pastpaperskenya.app.business.model.ResourceOne
import com.pastpaperskenya.app.business.model.ApiResponse

interface PaymentsRepository {
    suspend fun makeLnmoRequest(
        amount: Int,
        phone:String,
        customerId: String,
        accountRef: String
    ) : ResourceOne<ApiResponse>
}

