package com.pastpaperskenya.app.business.model.lipanampesa

data class PaymentDetails(
    val orderId: String,
    val merchantRequestId: String,
    val checkoutRequestId: String,
    val customerFirebaseId: String,
    val customerId: String
)
