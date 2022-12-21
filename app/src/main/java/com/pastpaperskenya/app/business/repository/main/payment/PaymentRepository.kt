package com.pastpaperskenya.app.business.repository.main.payment

import com.pastpaperskenya.app.business.datasources.remote.BaseDataSource
import com.pastpaperskenya.app.business.datasources.remote.services.main.RetrofitApiService
import com.pastpaperskenya.app.business.model.lipanampesa.PaymentDetails
import com.pastpaperskenya.app.business.model.mpesa.CheckMpesaPaymentStatus
import com.pastpaperskenya.app.business.model.mpesa.MpesaPaymentReqResponse
import com.pastpaperskenya.app.business.model.mpesa.MpesaTokenResponse
import com.pastpaperskenya.app.business.usecases.FirestoreUserService
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val retrofitApiService: RetrofitApiService,
    private val firestoreUserService: FirestoreUserService
): BaseDataSource() {


    suspend fun getMpesaToken(): NetworkResult<MpesaTokenResponse> {
        return safeApiCall { retrofitApiService.getMpesaToken() }
    }

    suspend fun createStkPush(total: String, phone: String, order_id: String, accesstoken:String): NetworkResult<MpesaPaymentReqResponse> {
        return safeApiCall { retrofitApiService.stkPushRequest(total, phone, order_id, accesstoken) }
    }

    suspend fun checkPaymentStatus(checkoutId: String, accesstoken: String): Response<CheckMpesaPaymentStatus> =
        retrofitApiService.checkPaymentStatus(checkoutId, accesstoken)

    suspend fun savePendingPaymentFirebase(paymentDetails: PaymentDetails)=
        firestoreUserService.savePendingPaymentFirebase(paymentDetails)
}