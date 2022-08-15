package com.pastpaperskenya.app.business.services.payment

import com.pastpaperskenya.app.business.model.lipanampesa.Payment


interface PaymentService {
    suspend fun getPayments(): List<Payment>
}