package com.pastpaperskenya.app.business.datasources.cache

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.model.category.CategoryImage
import com.pastpaperskenya.app.business.model.download.FileData
import com.pastpaperskenya.app.business.model.orders.*
import com.pastpaperskenya.app.business.model.product.*
import java.lang.reflect.Type


class Converters {

    /* global gson */
    var gson= Gson()

    /* start categories converter */
    @TypeConverter
    fun toImageJson(categoryImage: CategoryImage?): String? {
        return gson.toJson(
            categoryImage, object : TypeToken<CategoryImage?>(){}.type
        )
    }

    @TypeConverter
    fun fromImageJson(categoryImage: String?): CategoryImage? {
        return gson.fromJson<CategoryImage>(
            categoryImage, object :TypeToken<CategoryImage?>(){}.type
        )
    }

    /* end categories converter */

    /* start Downloads Converter */
    @TypeConverter
    fun toDownloadJson(file: FileData?): String? {
        return gson.toJson(
            file, object : TypeToken<FileData?>(){}.type
        )
    }

    @TypeConverter
    fun fromDownloadJson(file: String?): FileData? {
        return gson.fromJson<FileData>(
            file, object :TypeToken<FileData?>(){}.type
        )
    }

    /*end Downloads Converter */

    /* start products converter */

    @TypeConverter
    fun toProductDownloadJson(downloads: ArrayList<DownloadsProperty>?):String? {
        return gson.toJson(
            downloads, object : TypeToken<ArrayList<DownloadsProperty>?>(){}.type
        )
    }

    @TypeConverter
    fun fromProductDownloadJson(downloads: String?):ArrayList<DownloadsProperty>?{
        return gson.fromJson(
            downloads, object : TypeToken<ArrayList<DownloadsProperty>?>(){}.type
        )
    }

    @TypeConverter
    fun toProductDimensionsJson(dimensions: DimensionsProperty?):String? {
        return gson.toJson(
            dimensions, object : TypeToken<DimensionsProperty?>(){}.type
        )
    }

    @TypeConverter
    fun fromProductDimensionsJson(dimensions: String?):DimensionsProperty?{
        return gson.fromJson(
            dimensions, object : TypeToken<DimensionsProperty?>(){}.type
        )
    }


    @TypeConverter
    fun toProductCategoryJson(value: ArrayList<ProductCategory>?):String? {
        return gson.toJson(
            value, object : TypeToken<ArrayList<ProductCategory>?>(){}.type
        )
    }

    @TypeConverter
    fun fromProductCategoryJson(value: String?):ArrayList<ProductCategory>?{
        return gson.fromJson(
            value, object : TypeToken<ArrayList<ProductCategory>?>(){}.type
        )
    }

    @TypeConverter
    fun toProductTagsJson(value: ArrayList<TagsProperty>?):String? {
        return gson.toJson(
            value, object : TypeToken<ArrayList<TagsProperty>?>(){}.type
        )
    }

    @TypeConverter
    fun fromProductTagsJson(tags: String?):ArrayList<TagsProperty>?{
        return gson.fromJson(
            tags, object : TypeToken<ArrayList<TagsProperty>?>(){}.type
        )
    }

    @TypeConverter
    fun toProductImageJson(image: ArrayList<ImageProperty>?):String? {
        return gson.toJson(
            image, object : TypeToken<ArrayList<ImageProperty>?>(){}.type
        )
    }

    @TypeConverter
    fun fromProductImageJson(image: String?):ArrayList<ImageProperty>?{
        return gson.fromJson(
            image, object : TypeToken<ArrayList<ImageProperty>?>(){}.type
        )
    }

    @TypeConverter
    fun toProductAttributesJson(value: ArrayList<AttributesProperty>?):String? {
        return gson.toJson(
            value, object : TypeToken<ArrayList<AttributesProperty>?>(){}.type
        )
    }

    @TypeConverter
    fun fromProductAttributesJson(value: String?):ArrayList<AttributesProperty>?{
        return gson.fromJson(
            value, object : TypeToken<ArrayList<AttributesProperty>?>(){}.type
        )
    }

    @TypeConverter
    fun toProductDefaultPropertyJson(value: ArrayList<DefaultProperty>?):String? {
        return gson.toJson(
            value, object : TypeToken<ArrayList<DefaultProperty>?>(){}.type
        )
    }

    @TypeConverter
    fun fromProductDefaultPropertyJson(value: String?):ArrayList<DefaultProperty>?{
        return gson.fromJson(
            value, object : TypeToken<ArrayList<DefaultProperty>?>(){}.type
        )
    }

    @TypeConverter
    fun toProductMetaDataPropertyJson(value: ArrayList<MetaDataProperty>?):String? {
        return gson.toJson(
            value, object : TypeToken<ArrayList<MetaDataProperty>?>(){}.type
        )
    }

    @TypeConverter
    fun fromProductMetaDataPropertyJson(value: String?):ArrayList<MetaDataProperty>?{
        return gson.fromJson(
            value, object : TypeToken<ArrayList<MetaDataProperty>?>(){}.type
        )
    }

    @TypeConverter
    fun getId(values: String?): ArrayList<Int>? {
        val list= ArrayList<Int>()

        val array= values?.split(",".toRegex())?.dropLastWhile {
            it.isEmpty()
        }?.toTypedArray()

        for (s in array!!) {
            if (s.isNotEmpty()) {
                list.add(s.toInt())
            }
        }
        return list
    }

    @TypeConverter
    fun writeIds(list: ArrayList<Int>?):String? {
        var ids= ""
        for(i in list!!) ids += ",$i"
        return ids
    }
    /*end products converter */


    /* orders converter */

    @TypeConverter
    fun toOrderBillingPropertiesJson(value: OrderBillingProperties): String?{
        return gson.toJson(
            value, object : TypeToken<OrderBillingProperties?>(){}.type
        )
    }

    @TypeConverter
    fun fromOrderBillingProperties(value: String?): OrderBillingProperties?{
        return gson.fromJson(
            value, object : TypeToken<OrderBillingProperties?>(){}.type
        )
    }

    @TypeConverter
    fun toOrderMetaDataJson(value: ArrayList<OrderMetaData>): String?{
        return gson.toJson(
            value, object : TypeToken<ArrayList<OrderMetaData>?>(){}.type
        )
    }

    @TypeConverter
    fun fromOrderMetaData(value: String?): ArrayList<OrderMetaData>?{
        return gson.fromJson(
            value, object : TypeToken<ArrayList<OrderMetaData>?>(){}.type
        )
    }

    @TypeConverter
    fun toOrderLineItemsJson(value: ArrayList<OrderLineItems>): String?{
        return gson.toJson(
            value, object : TypeToken<ArrayList<OrderLineItems>?>(){}.type
        )
    }

    @TypeConverter
    fun fromOrderLineItems(value: String?): ArrayList<OrderLineItems>?{
        return gson.fromJson(
            value, object : TypeToken<ArrayList<OrderLineItems>?>(){}.type
        )
    }

    @TypeConverter
    fun toOrderTaxLinesJson(value: ArrayList<OrderTaxLines>): String?{
        return gson.toJson(
            value, object : TypeToken<ArrayList<OrderTaxLines>?>(){}.type
        )
    }

    @TypeConverter
    fun fromOrderTaxLines(value: String?): ArrayList<OrderTaxLines>?{
        return gson.fromJson(
            value, object : TypeToken<ArrayList<OrderTaxLines>?>(){}.type
        )
    }

    @TypeConverter
    fun toOrderFeeLinesJson(value: ArrayList<OrderFeeLines>): String?{
        return gson.toJson(
            value, object : TypeToken<ArrayList<OrderFeeLines>?>(){}.type
        )
    }

    @TypeConverter
    fun fromOrderFeeLines(value: String?): ArrayList<OrderFeeLines>?{
        return gson.fromJson(
            value, object : TypeToken<ArrayList<OrderFeeLines>?>(){}.type
        )
    }

    @TypeConverter
    fun toOrderCouponLinesJson(value: ArrayList<OrderCouponLines>): String?{
        return gson.toJson(
            value, object : TypeToken<ArrayList<OrderCouponLines>?>(){}.type
        )
    }

    @TypeConverter
    fun fromOrderCouponLines(value: String?): ArrayList<OrderCouponLines>?{
        return gson.fromJson(
            value, object : TypeToken<ArrayList<OrderCouponLines>?>(){}.type
        )
    }

    @TypeConverter
    fun toOrderRefundsJson(value: ArrayList<OrderRefunds>): String?{
        return gson.toJson(
            value, object : TypeToken<ArrayList<OrderRefunds>?>(){}.type
        )
    }

    @TypeConverter
    fun fromOrderRefunds(value: String?): ArrayList<OrderRefunds>?{
        return gson.fromJson(
            value, object : TypeToken<ArrayList<OrderRefunds>?>(){}.type
        )
    }
    /* end of order converter*/

    /* start cart */

    @TypeConverter
    fun toCartJson(value: ArrayList<Cart>): String?{
        return gson.toJson(
            value, object : TypeToken<ArrayList<Cart>?>(){}.type
        )
    }

    @TypeConverter
    fun fromCart(value: String?): ArrayList<Cart>?{
        return gson.fromJson(
            value, object : TypeToken<ArrayList<Cart>?>(){}.type
        )
    }

    /* end cart */


}

