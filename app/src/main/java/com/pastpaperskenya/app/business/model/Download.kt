package com.pastpaperskenya.app.business.model

data class Download(
    val download_id: String,
    val download_url: String,
    val product_id: Int,
    val product_name:String,
    val download_name: String,
    val order_id:Int,
    val order_key: String,
    val downloads_remaining: String,
    val access_expires:String,
    val access_expires_gmt: String,
    val file: FileData
)
