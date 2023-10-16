package com.pastpaperskenya.papers.business.repository.main.payment

import com.pastpaperskenya.papers.business.datasources.remote.BaseDataSource
import com.pastpaperskenya.papers.business.datasources.remote.services.main.RetrofitApiService
import com.pastpaperskenya.papers.business.model.mpesa.MpesaPaymentReqResponse
import com.pastpaperskenya.papers.business.model.mpesa.MpesaTokenResponse
import com.pastpaperskenya.papers.business.model.mpesa.Payment
import com.pastpaperskenya.papers.business.usecases.FirestoreUserService
import com.pastpaperskenya.papers.business.util.sealed.NetworkResult
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val retrofitApiService: RetrofitApiService,
    private val firestoreUserService: FirestoreUserService,
): BaseDataSource() {

    suspend fun getMpesaToken(): NetworkResult<MpesaTokenResponse> {
        return safeApiCall { retrofitApiService.getMpesaToken() }
    }

    suspend fun createStkPush(total: String, phone: String, order_id: String, accesstoken:String): NetworkResult<MpesaPaymentReqResponse> {
        return safeApiCall { retrofitApiService.stkPushRequest(total, phone, order_id, accesstoken) }
    }

    suspend fun savePaymentToFirebase(paymentDetails: Payment)=
        firestoreUserService.savePaymentToFirebase(paymentDetails)

    suspend fun savePendingPaymentToServer(payment: Payment): NetworkResult<Payment> {
        return safeApiCall { retrofitApiService.sendOrderData(payment) }
    }

}