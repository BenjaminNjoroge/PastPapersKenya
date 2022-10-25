package com.pastpaperskenya.app.business.model.tags

data class ProductTags(
    val id: Int,
    val name: String,
    val slug: String?,
    val description: String?,
    val count: Int?
)
