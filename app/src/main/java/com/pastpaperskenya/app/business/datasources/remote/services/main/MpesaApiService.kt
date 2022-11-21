package com.pastpaperskenya.app.business.datasources.remote.services.main

import retrofit2.http.PUT
import com.pastpaperskenya.app.business.model.mpesa.MpesaTokenResponse
import retrofit2.Call

interface MpesaApiService {
    @get:PUT("generateToken/")
    val mpesaToken: Call<MpesaTokenResponse?>?
}