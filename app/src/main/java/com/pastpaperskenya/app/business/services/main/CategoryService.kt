package com.pastpaperskenya.app.business.services.main

import com.pastpaperskenya.app.business.model.Category
import com.pastpaperskenya.app.business.util.Constants.API_PRODUCT_CATEGORIES
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CategoryService {

    @GET(API_PRODUCT_CATEGORIES)
    suspend fun getCategories(
        @Query ("parent") parent: Int
    ): List<Category>
}