package com.pastpaperskenya.app.business.model.orders

data class OrderTaxLines(
    val id: Int,
    val rate_code: String,
    val rate_id: String,
    val label: String,
    val compound: Boolean,
    val tax_total: String
)
