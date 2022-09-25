package com.pastpaperskenya.app.business.services.main

import com.pastpaperskenya.app.business.model.category.HomeCategory
import com.pastpaperskenya.app.business.model.Download
import com.pastpaperskenya.app.business.model.category.SliderCategory
import com.pastpaperskenya.app.business.model.auth.Customer
import com.pastpaperskenya.app.business.model.category.SubCategory
import com.pastpaperskenya.app.business.model.product.Product
import com.pastpaperskenya.app.business.model.product.ProductTag
import com.pastpaperskenya.app.business.util.Constants.*
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

    @GET(API_PRODUCT_TAGS)
    suspend fun getProductTags(
        @Query ("per_page") perpage: Int
    ): Response<List<ProductTag>>


    @GET(API_PRODUCTS)
    suspend fun getProductList(
        @Query ("per_page") perpage: Int,
        @Query ("category") category: String
    ): Response<List<Product>>

    //for download
    @GET(API_DOWNLOAD)
    suspend fun getDownloads(
        @Path("id") id: Int?
    ): List<Download>
}