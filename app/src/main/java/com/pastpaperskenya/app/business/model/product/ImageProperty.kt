package com.pastpaperskenya.app.business.model.product

data class ImageProperty(
    val id: Int,
    val date_created: String,
    val date_created_gmt: String,
    val date_modified: String,
    val date_modified_gmt: String,
    val src:String,
    val name: String,
    val alt: String,

)
