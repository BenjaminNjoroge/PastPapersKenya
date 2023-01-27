package com.pastpaperskenya.papers.business.model.user

import com.google.gson.annotations.SerializedName

data class CustomerUpdate (
    @SerializedName("first_name")
    val firstname: String?=null,

    @SerializedName("last_name")
    val lastname: String?=null,

    @SerializedName("billing")
    val billing: CustomerBilling?=null,

    @SerializedName("avatar_url")
    val profileImage: String?=null
    )

data class CustomerBilling (
    @SerializedName("state")
    val county: String,

    @SerializedName("country")
    val country: String,

    @SerializedName("phone")
    val phone: String
        )
