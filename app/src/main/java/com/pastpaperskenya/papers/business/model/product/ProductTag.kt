package com.pastpaperskenya.papers.business.model.product

data class ProductTag(
    val id: Int,
    val name: String,
    val slug: String,
    val description: String,
    val count: Int
)
