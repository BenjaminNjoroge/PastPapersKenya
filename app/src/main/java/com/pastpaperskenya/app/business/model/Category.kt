package com.pastpaperskenya.app.business.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.pastpaperskenya.app.business.cache.Converters

@Entity(tableName = "categories")
data class Category (

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
    @TypeConverters(Converters::class)
    val image: CategoryImage?,

    @SerializedName("menu_order")
    val menu_order: Int?,

    @SerializedName("count")
    val count: Int?
        )

