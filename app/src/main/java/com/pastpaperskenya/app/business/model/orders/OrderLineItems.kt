package com.pastpaperskenya.app.business.model.orders

data class OrderLineItems (
    val id: Int,
    val name: String,
    val product_id: Int,
    val variation_id: Int,
    val quantity: Int,
    val tax_class: String,
    val subtotal: String,
    val total: String,
    val subtotal_tax: String,
    val total_tax: String,
    val sku: String,
    val price: String
        )