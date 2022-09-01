package com.pastpaperskenya.app.business.services.main

import javax.inject.Inject

class CategoryRemoteDataSource @Inject constructor(
    private val categoryService: CategoryService
): BaseDataSource(){

    suspend fun getCategories() = getResult {
        categoryService.getCategories()
    }
}