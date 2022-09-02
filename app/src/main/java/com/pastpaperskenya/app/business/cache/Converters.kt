package com.pastpaperskenya.app.business.cache

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pastpaperskenya.app.business.model.CategoryImage


class Converters {

    var gson= Gson()
    @TypeConverter
    fun toImageJson(categoryImage: CategoryImage): String? {
        return gson.toJson(
            categoryImage, object : TypeToken<CategoryImage>(){}.type
        )
    }

    @TypeConverter
    fun fromImageJson(image: String): CategoryImage? {
        return gson.fromJson<CategoryImage>(
            image, object :TypeToken<CategoryImage>(){}.type
        )
    }
}