package com.pastpaperskenya.app.business.model.orders

import com.google.gson.annotations.SerializedName

data class CreateOrder(

    @SerializedName("customer_id")
    val customerId: Int,

    @SerializedName("payment_method")
    val paymentMethod: String,

    @SerializedName("payment_method_title")
    val paymentMethodTitle: String,

    @SerializedName("set_paid")
    val setPaid: Boolean?,

    @SerializedName("billing")
    val billing: OrderBillingProperties?,

    @SerializedName("line_items")
    val lineItems: ArrayList<OrderLineItems>

)
