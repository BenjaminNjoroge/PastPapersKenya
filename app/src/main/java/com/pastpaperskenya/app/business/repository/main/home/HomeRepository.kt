package com.pastpaperskenya.app.business.repository.main.home

import com.pastpaperskenya.app.business.services.main.CategoryService
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val categoryService: CategoryService
) {

    suspend fun getCategories()= flow{
        emit(NetworkResult.Loading(true))
        val categoryResponse= categoryService.getCategories()
        emit(NetworkResult.Success(categoryResponse))
    }.catch { e->
        emit(NetworkResult.Error(e.message ?:"Unknown Error"))
    }
}