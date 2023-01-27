package com.pastpaperskenya.papers.business.model.orders

data class OrderCouponLines(
    val id: Int,
    val code: String,
    val discount: String,
    val discount_tax: String
)
