package com.pastpaperskenya.app.business.model

data class UserDetails(
    val userId:String,
    val email: String?,
    val phone: String?,
    val firstname: String?,
    val lastname: String?,
    val country: String?,
    val county: String?
)
