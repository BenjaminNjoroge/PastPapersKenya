package com.pastpaperskenya.app.business.model.user

import com.google.gson.annotations.SerializedName

data class CustomerUpdate (
    @SerializedName("first_name")
    val firstname: String?,

    @SerializedName("last_name")
    val lastname: String?,

    @SerializedName("billing")
    val billing: CustomerBilling?
    )

data class CustomerBilling (
    @SerializedName("state")
    val county: String,

    @SerializedName("country")
    val country: String,

    @SerializedName("phone")
    val phone: String
        )
