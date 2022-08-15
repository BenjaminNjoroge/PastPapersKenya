package com.pastpaperskenya.app.business.repository.main

interface UserDetailsRepository {

    suspend fun userDetails(
        userId: String,
        email: String,
        phone: String,
        firstname: String,
        lastname: String,
        county: String,
        country: String
    )
}