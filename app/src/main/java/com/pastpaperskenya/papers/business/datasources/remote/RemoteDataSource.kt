package com.pastpaperskenya.papers.business.datasources.remote

import com.pastpaperskenya.papers.business.datasources.remote.services.main.RetrofitApiService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val retrofitApiService: RetrofitApiService) : BaseDataSource() {

    suspend fun getRemoteParentCategory(parent:Int, filter: ArrayList<Int>)=
        getResult { retrofitApiService.getRemoteHomeCategory(parent, filter) }

    suspend fun getRemoteSliderCategory(parent:Int)=
        getResult { retrofitApiService.getRemoteSliderCategory(parent) }

    suspend fun getRemoteSubCategory(parent:Int, perpage:Int)=
        getResult { retrofitApiService.getRemoteSubCategory(parent, perpage) }

    suspend fun getProductDetail(id: Int)=
        getResult { retrofitApiService.getProductDetail(id)}

    suspend fun getMyOrderDetails( id: Int)=
        getResult { retrofitApiService.getMyOrderDetails(id) }

    suspend fun getMyOrders(id:Int)=
        getResult { retrofitApiService.getMyOrders(id)}
}