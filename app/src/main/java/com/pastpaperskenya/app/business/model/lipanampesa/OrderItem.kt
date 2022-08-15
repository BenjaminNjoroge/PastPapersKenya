package com.pastpaperskenya.app.business.model.lipanampesa

data class OrderItem(
    var id: Int,
    var itemName: String,
    var itemQty: Int,
    var itemPrice: Int,
    var itemCategory: String,
)
