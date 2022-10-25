package com.pastpaperskenya.app.business.datasources.remote

import com.pastpaperskenya.app.business.datasources.remote.services.main.RetrofitService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val retrofitService: RetrofitService) : BaseDataSource() {

    suspend fun getRemoteParentCategory(parent:Int, filter: ArrayList<Int>)=
        getResult { retrofitService.getRemoteHomeCategory(parent, filter) }

    suspend fun getRemoteSliderCategory(parent:Int)=
        getResult { retrofitService.getRemoteSliderCategory(parent) }

    suspend fun getRemoteSubCategory(parent:Int, perpage:Int)=
        getResult { retrofitService.getRemoteSubCategory(parent, perpage) }

    suspend fun getProductDetail(id: Int)=
        getResult { retrofitService.getProductDetail(id)}

    suspend fun getMyOrderDetails( id: Int)=
        getResult { retrofitService.getMyOrderDetails(id) }


}