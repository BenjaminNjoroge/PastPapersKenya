package com.pastpaperskenya.papers.business.model.mpesa


data class Payment(
    val checkout_request_id: String?=null,
    val merchant_request_id: String?=null,
    val customer_id: Int?=null,
    val transaction_date: String?=null,
    val order_id: Int?=null,
    val amount: Double?= null,
    val result_desc: String?=null,
    val result_code: Int?=null,
    val status: String?=null,
    val mpesa_receipt_number: String?=null,
    val phone_number: String?=null,
    val customer_firebase_id: String?=null,
    val email: String?=null
)

