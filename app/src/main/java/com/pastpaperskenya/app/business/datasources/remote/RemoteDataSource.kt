package com.pastpaperskenya.app.business.datasources.remote

import com.pastpaperskenya.app.business.services.main.RetrofitService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val retrofitService: RetrofitService
) : BaseDataSource() {

    suspend fun getRemoteParentCategory(parent:Int, filter: ArrayList<Int>)=
        getResult { retrofitService.getRemoteHomeCategory(parent, filter) }

    suspend fun getRemoteSliderCategory(parent:Int)=
        getResult { retrofitService.getRemoteSliderCategory(parent) }

    suspend fun getRemoteSubCategory(parent:Int, perpage:Int)=
        getResult { retrofitService.getRemoteSubCategory(parent, perpage) }

    suspend fun getRemoteDownloads(id: Int)=
         retrofitService.getDownloads(id)

}