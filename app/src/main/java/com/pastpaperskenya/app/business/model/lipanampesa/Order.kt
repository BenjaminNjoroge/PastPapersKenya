package com.pastpaperskenya.app.business.model.lipanampesa

import android.os.Parcelable
import com.google.firebase.firestore.FieldValue
import kotlinx.android.parcel.Parcelize
import java.util.*

internal data class Order(
    val orderId :String,
    val customerId: String,
    val country: String,
    val county: String,
    val itemsCount : Int,
    val totalPrice :Int,
    val status : OrderStatus = OrderStatus.PENDING,
    val comments :String,
    val date :Date= Date(),
    val paymentMethod : String,
    val paymentStatus :String= "unpaid",
){
    fun toMap(): MutableMap<String, Any>{
        val orderMap= mutableMapOf<String, Any>()

        orderMap[DatabaseKeys.Order.orderId]= orderId
        orderMap[DatabaseKeys.Order.customerId]= customerId
        orderMap[DatabaseKeys.Order.itemsCount]= itemsCount
        orderMap[DatabaseKeys.Order.totalPrice]= totalPrice
        orderMap[DatabaseKeys.Order.status]= status
        orderMap[DatabaseKeys.Order.comments]= comments
        orderMap[DatabaseKeys.Order.date]= FieldValue.serverTimestamp()
        orderMap[DatabaseKeys.Order.paymentMethod]= paymentMethod
        orderMap[DatabaseKeys.Order.paymentStatus]= paymentStatus
        orderMap[DatabaseKeys.Order.country]= country
        orderMap[DatabaseKeys.Order.county]= county

        return orderMap
    }
}
