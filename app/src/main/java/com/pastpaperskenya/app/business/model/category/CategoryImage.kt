package com.pastpaperskenya.app.business.model.category

import com.google.gson.annotations.SerializedName

data class CategoryImage(
    val id: Int?,
    val date_created: String?,
    val date_created_gmt: String?,
    val date_modified: String?,
    val date_modified_gmt: String?,
    val src: String?,
    val name: String?,
    val alt: String?
)
