package com.pastpaperskenya.app.business.repository.main.payment

import com.pastpaperskenya.app.business.datasources.remote.BaseDataSource
import com.pastpaperskenya.app.business.datasources.remote.services.main.RetrofitApiService
import com.pastpaperskenya.app.business.model.mpesa.CheckMpesaPaymentStatus
import com.pastpaperskenya.app.business.model.mpesa.MpesaPaymentReqResponse
import com.pastpaperskenya.app.business.model.mpesa.MpesaTokenResponse
import com.pastpaperskenya.app.business.util.sealed.Resource
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val retrofitApiService: RetrofitApiService
): BaseDataSource() {


    suspend fun getMpesaToken(): Resource<MpesaTokenResponse> {
        return safeApiCall { retrofitApiService.getMpesaToken() }
    }

    suspend fun createStkPush(total: String, phone: String, order_id: String, accesstoken:String): Resource<MpesaPaymentReqResponse> {
        return safeApiCall { retrofitApiService.stkPushRequest(total, phone, order_id, accesstoken) }
    }

    suspend fun checkPaymentStatus(checkoutId: String, accesstoken: String): Resource<CheckMpesaPaymentStatus> {
        return safeApiCall { retrofitApiService.checkPaymentStatus(checkoutId, accesstoken) }
    }

}