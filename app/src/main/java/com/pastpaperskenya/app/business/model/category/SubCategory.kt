package com.pastpaperskenya.app.business.model.category

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "subcategory")
data class SubCategory (

    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String?,

    @SerializedName("slug")
    val slug: String?,

    @SerializedName("parent")
    val parent: Int?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("display")
    val display: String?,

    @SerializedName("image")
    val image: CategoryImage?,

    @SerializedName("menu_order")
    val menu_order: Int?,

    @SerializedName("count")
    val count: Int?
        )


