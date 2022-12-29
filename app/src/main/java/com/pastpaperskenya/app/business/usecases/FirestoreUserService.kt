package com.pastpaperskenya.app.business.usecases

import com.pastpaperskenya.app.business.model.mpesa.Payment
import com.pastpaperskenya.app.business.model.user.UserDetails

interface FirestoreUserService {

    suspend fun saveUserDetails(userDetails: UserDetails)

    suspend fun updateUserDetails(
        userId: String,
        phone: String?,
        firstname: String?,
        lastname: String?,
        country: String?,
        county: String?,
    )

    suspend fun getFirestoreUserDetails(userId: String): UserDetails?

    suspend fun savePendingPaymentFirebase(paymentDetails: Payment)

}