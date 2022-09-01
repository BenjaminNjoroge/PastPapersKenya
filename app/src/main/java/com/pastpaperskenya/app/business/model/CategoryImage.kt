package com.pastpaperskenya.app.business.model

import com.google.gson.annotations.SerializedName

data class CategoryImage(
    @SerializedName("id")
    val id: Int,

    @SerializedName("date_created")
    val date_created: String?,

    @SerializedName("date_created_gmt")
    val date_created_gmt: String?,

    @SerializedName("date_modified")
    val date_modified: String?,

    @SerializedName("date_modified_gmt")
    val date_modified_gmt: String?,

    @SerializedName("src")
    val src: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("alt")
    val alt: String?
)
