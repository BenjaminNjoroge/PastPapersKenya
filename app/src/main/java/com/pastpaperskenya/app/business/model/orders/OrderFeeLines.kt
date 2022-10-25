package com.pastpaperskenya.app.business.model.orders

data class OrderFeeLines(
    val id: Int,
    val method_title: String,
    val method_id: String,
    val total: String,
    val total_tax: String
)
