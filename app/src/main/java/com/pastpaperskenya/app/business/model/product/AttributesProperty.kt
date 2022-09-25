package com.pastpaperskenya.app.business.model.product

data class AttributesProperty(
    val id: Int,
    val name: String,
    val position: Int,
    val visible: Boolean,
    val variation: Boolean,
    val options: ArrayList<String>
)
