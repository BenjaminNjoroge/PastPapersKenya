package com.pastpaperskenya.app.business.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse (
    @SerializedName("message")
    val message: String
        )