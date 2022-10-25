package com.pastpaperskenya.app.business.model.orders

data class OrderBillingProperties (
    val first_name: String,
    val last_name: String,
    val company: String,
    val address_1: String,
    val address_2: String,
    val city: String,
    val state: String,
    val postcode: String,
    val country: String,
    val email: String,
    val phone: String
        )