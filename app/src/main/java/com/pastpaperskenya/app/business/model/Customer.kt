package com.pastpaperskenya.app.business.model

import com.google.firebase.firestore.Exclude
import com.google.gson.annotations.SerializedName


data class Customer (

    @SerializedName("id")
    val id: Int,

    @SerializedName("date_created")
    val date_created: String,

    @SerializedName("date_created_gmt")
    val date_created_gmt: String,

    @SerializedName("date_modified")
    val date_modified: String,

    @SerializedName("date_modified_gmt")
    val date_modified_gmt: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("first_name")
    val first_name: String,

    @SerializedName("last_name")
    val last_name: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("avatar_url")
    val avatar_url: String,

    @SerializedName("billing")
    val billingAddress: BillingAddress

)