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

    //for home page
    @GET(API_PRODUCT_CATEGORIES)
    suspend fun getParentCategories(
        @Query ("parent") parent: Int
    ): List<Category>


    //for download
    @GET(API_DOWNLOAD)
    suspend fun getDownloads(
        @Path("id") id: Int
    ): List<Download>
}