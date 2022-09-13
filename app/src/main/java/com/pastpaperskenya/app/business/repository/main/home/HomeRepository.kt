package com.pastpaperskenya.app.business.repository.main.home

import com.pastpaperskenya.app.business.services.main.RetrofitService
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val categoryService: RetrofitService,
) {

    suspend fun getCategories(parent: Int, slider: ArrayList<Int>)= flow{
        emit(NetworkResult.Loading(true))
        val categoryResponse= categoryService.getParentCategories(parent, slider)
        emit(NetworkResult.Success(categoryResponse))
    }.catch { e->
        emit(NetworkResult.Error(e.message ?:"Unknown Error"))
    }

    suspend fun getSliderCategories(parent: Int)= flow{
        emit(NetworkResult.Loading(true))
        val sliderImage= categoryService.getSliderCategories(parent)
        emit(NetworkResult.Success(sliderImage))
    }.catch { e->
        emit(NetworkResult.Error(e.message ?: "Unknown Error"))
    }
}