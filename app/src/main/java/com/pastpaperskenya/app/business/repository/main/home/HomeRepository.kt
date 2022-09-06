package com.pastpaperskenya.app.business.repository.main.home

import com.pastpaperskenya.app.business.services.main.RetrofitService
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val categoryService: RetrofitService
) {

    suspend fun getCategories(parent: Int)= flow{
        emit(NetworkResult.Loading(true))
        val categoryResponse= categoryService.getParentCategories(parent)
        emit(NetworkResult.Success(categoryResponse))
    }.catch { e->
        emit(NetworkResult.Error(e.message ?:"Unknown Error"))
    }
}