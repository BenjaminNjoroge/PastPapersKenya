package com.pastpaperskenya.papers.business.usecases

import com.pastpaperskenya.papers.business.model.mpesa.Payment
import com.pastpaperskenya.papers.business.model.user.UserDetails

interface FirestoreUserService {

    suspend fun saveUserDetails(userDetails: UserDetails)

    suspend fun updateUserDetails(
        userId: String,
        phone: String?,
        firstname: String?,
        lastname: String?,
        country: String?,
        county: String?,
        email: String
    )

    suspend fun getFirestoreUserDetails(email: String): UserDetails?

    suspend fun savePaymentToFirebase(paymentDetails: Payment)

}