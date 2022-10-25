package com.pastpaperskenya.app.business.model.orders

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Orders (
    @PrimaryKey
    val id: Int,
    val parent_id: Int?,
    val number: String?,
    val order_key:String?,
    val created_via: String?,
    val version: String?,
    val status: String?,
    val currency: String?,
    val date_created:String?,
    val date_created_gmt: String?,
    val date_modified: String?,
    val date_modified_gmt: String?,
    val discount_total: String?,
    val discount_tax: String?,
    val shipping_tax: String?,
    val cart_tax: String?,
    val total: String?,
    val total_tax:String?,
    val prices_include_tax: Boolean?,
    val customer_id: Int?,
    val customer_ip_address: String?,
    val customer_user_agent : String?,
    val customer_note: String?,
    val billing: OrderBillingProperties?,
    val payment_method: String?,
    val payment_method_title: String?,
    val transaction_id: String?,
    val date_paid: String?,
    val date_paid_gmt: String?,
    val date_completed: String?,
    val date_completed_gmt: String?,
    val cart_hash: String?,
    val meta_data: ArrayList<OrderMetaData>?,
    val line_items: ArrayList<OrderLineItems>?,
    val tax_lines: ArrayList<OrderTaxLines>?,
    val fee_lines: ArrayList<OrderFeeLines>?,
    val coupon_lines: ArrayList<OrderCouponLines>?,
    val refunds: ArrayList<OrderRefunds>?,
    val set_paid: Boolean?

        )