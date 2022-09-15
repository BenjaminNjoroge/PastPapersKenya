package com.pastpaperskenya.app.business.services.main

import com.pastpaperskenya.app.business.model.Category
import com.pastpaperskenya.app.business.model.Download
import com.pastpaperskenya.app.business.model.auth.Customer
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
    suspend fun getRemoteCategory(
        @Query ("parent") parent: Int,
        @Query ("exclude") filter: ArrayList<Int>
    ): Response<List<Category>>

//    @GET(API_PRODUCT_CATEGORIES)
//    suspend fun getSliderCategories(
//        @Query ("parent") parent: Int
//    ): Response<List<Category>>


    //for download
    @GET(API_DOWNLOAD)
    suspend fun getDownloads(
        @Path("id") id: Int?
    ): List<Download>
}