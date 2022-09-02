package com.pastpaperskenya.app.business.repository.main.home

import com.pastpaperskenya.app.business.util.sealed.ResourceOne
import com.pastpaperskenya.app.business.model.auth.ApiResponse

interface PaymentsRepository {

    suspend fun makeLnmoRequest(
        amount: Int,
        phone:String,
        customerId: String,
        accountRef: String
    ) : ResourceOne<ApiResponse>
}

