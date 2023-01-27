package com.pastpaperskenya.papers.business.model.orders

import com.google.gson.annotations.SerializedName

data class CreateOrder(

    @SerializedName("id")
    val orderId: Int?=null,

    @SerializedName("number")
    val number:String?=null,

    @SerializedName("customer_id")
    val customerId: Int?= null,

    @SerializedName("payment_method")
    val paymentMethod: String?=null,

    @SerializedName("payment_method_title")
    val paymentMethodTitle: String?= null,

    @SerializedName("set_paid")
    val setPaid: Boolean?= null,

    @SerializedName("billing")
    val billing: OrderBillingProperties?= null,

    @SerializedName("line_items")
    val lineItems: ArrayList<OrderLineItems>?= null

)
