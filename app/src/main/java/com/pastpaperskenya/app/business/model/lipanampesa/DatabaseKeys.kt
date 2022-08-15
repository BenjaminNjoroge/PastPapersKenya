package com.pastpaperskenya.app.business.model.lipanampesa

object DatabaseKeys {

    object User {
        const val userId = "id"
        const val email = "email"
        const val phone = "phone"
        const val firstname = "firstname"
        const val lastname= "lastname"
        const val country= "country"
        const val county= "county"
        const val fcmToken = "fcmToken"
    }

    object Payment {
        const val checkoutRequestId = "checkoutRequestId"
        const val merchantRequestId = "merchantRequestId"
        const val orderId = "orderId"
        const val customerId = "customerId"
        const val resultDesc = "resultDesc"
        const val date = "date"
        const val status = "status"
        const val mpesaReceiptNumber = "mpesaReceiptNumber"
        const val phoneNumber = "phoneNumber"
    }

    object Order {
        const val orderId = "orderId"
        const val customerId = "customerId"
        const val country = "country"
        const val county= "county"
        const val itemsCount = "itemsCount"
        const val totalPrice = "totalPrice"
        const val status = "status"
        const val comments = "comments"
        const val date = "date"
        const val paymentMethod = "paymentMethod"
        const val paymentStatus = "paymentStatus"
        const val fcmToken = "fcmToken"
    }

    object OrderItem {
        const val itemName = "itemName"
        const val itemQty = "itemQty"
        const val itemCategory = "itemCategory"
        const val itemPrice = "itemPrice"
    }
}