package com.pastpaperskenya.papers.business.model.user

import com.google.gson.annotations.SerializedName

data class Customer (

    @SerializedName("id")
    val id: Int?,

    @SerializedName("date_created")
    val date_created: String?,

    @SerializedName("date_created_gmt")
    val date_created_gmt: String?,

    @SerializedName("date_modified")
    val date_modified: String?,

    @SerializedName("date_modified_gmt")
    val date_modified_gmt: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("first_name")
    val firstname: String?,

    @SerializedName("last_name")
    val lastname: String?,

    @SerializedName("username")
    val username: String?,

    @SerializedName("password")
    val password: String?,

    @SerializedName("role")
    val role: String?,

    @SerializedName("avatar_url")
    val image: String?,

    @SerializedName("billing")
    val billingAddress: BillingAddress?

) {
    class BillingAddress (
        val firstname: String?,
        val lastname: String?,
        val company: String?,
        val address: String?,
        val city: String?,
        val state: String?,
        val country: String?,
        val email: String?,
        val phone: String?
            )
}