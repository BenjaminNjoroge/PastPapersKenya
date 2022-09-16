package com.pastpaperskenya.app.business.datasources.remote

import com.pastpaperskenya.app.business.services.main.RetrofitService
import javax.inject.Inject

class CategoryRemoteDataSource @Inject constructor(
    private val retrofitService: RetrofitService
) : BaseDataSource() {

    suspend fun getRemoteParentCategory(parent:Int, filter: ArrayList<Int>)=
        getResult { retrofitService.getRemoteHomeCategory(parent, filter) }

    suspend fun getRemoteSliderCategory(parent:Int)=
        getResult { retrofitService.getRemoteSliderCategory(parent) }

}