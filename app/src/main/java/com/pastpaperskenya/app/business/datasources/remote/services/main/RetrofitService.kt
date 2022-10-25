package com.pastpaperskenya.app.business.datasources.remote.services.main

import com.pastpaperskenya.app.business.model.category.HomeCategory
import com.pastpaperskenya.app.business.model.download.Download
import com.pastpaperskenya.app.business.model.category.SliderCategory
import com.pastpaperskenya.app.business.model.auth.Customer
import com.pastpaperskenya.app.business.model.category.SubCategory
import com.pastpaperskenya.app.business.model.orders.Orders
import com.pastpaperskenya.app.business.model.product.Product
import com.pastpaperskenya.app.business.model.product.ProductTag
import com.pastpaperskenya.app.business.util.Constants.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {

    //user
    @FormUrlEncoded
    @POST(API_CUSTOMER)
    suspend fun createUser(
        @Field ("email") email: String,
        @Field ("first_name") firstname: String,
        @Field ("last_name") lastname: String,
        @Field ("password") password: String
    ): Response<Customer>


    @GET(API_CUSTOMER)
    suspend fun getUser(
        @Query ("email") email: String
    ) : Response<List<Customer>>


    @GET(API_PRODUCT_CATEGORIES)
    suspend fun getRemoteHomeCategory(
        @Query ("parent") parent: Int,
        @Query ("exclude") filter: ArrayList<Int>
    ): Response<List<HomeCategory>>

    @GET(API_PRODUCT_CATEGORIES)
    suspend fun getRemoteSliderCategory(
        @Query ("parent") parent: Int
    ): Response<List<SliderCategory>>

    @GET(API_PRODUCT_CATEGORIES)
    suspend fun getRemoteSubCategory(
        @Query ("parent") parent: Int,
        @Query ("per_page") perpage: Int
    ): Response<List<SubCategory>>


    @GET(API_PRODUCT_TAGS_ID)
    suspend fun checkProductHasTagId(
        @Path("id") id: Int
    ):List<ProductTag>

    @GET(API_PRODUCT_FILTER_TAGS)
    suspend fun productFilterTag(
        @Query("per_page") perpage: Int,
        @Query("tag") tags: Int,
        @Query("categories") categories: Int
    )

    @GET(API_PRODUCTS)
    suspend fun getProductsList(
        @Query ("per_page") perpage: Int,
        @Query("categories") category: Int,
    ):List<Product>

    @GET(API_PRODUCT_DETAIL)
    suspend fun getProductDetail(
        @Path("id") id: Int?
    ): Response<Product>

    @GET(API_DOWNLOAD)
    suspend fun getDownloads(
        @Path("id") id: Int?
    ): List<Download>

    @GET(API_REQUEST_ORDER)
    suspend fun getMyOrders(
        @Query("customer") customer: Int?
    ): List<Orders>

    @GET(API_CANCEL_ORDER)
    suspend fun getMyOrderDetails(
        @Path("id") id: Int?
    ):Response<Orders>



}