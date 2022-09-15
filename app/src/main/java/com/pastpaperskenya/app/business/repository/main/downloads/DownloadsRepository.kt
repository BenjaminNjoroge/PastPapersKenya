package com.pastpaperskenya.app.business.repository.main.downloads

import com.pastpaperskenya.app.business.model.auth.UserDetails
import com.pastpaperskenya.app.business.services.auth.UserService
import com.pastpaperskenya.app.business.services.main.RetrofitService
import com.pastpaperskenya.app.business.util.sealed.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DownloadsRepository @Inject constructor(
    private val retrofitService: RetrofitService,
    private val userService: UserService){

//    suspend fun getDownloads(id: Int?) = flow{
//        emit(Resource.Loading(true))
//
//        val downloadsResponse= retrofitService.getDownloads(id)
//        emit(Resource.Success(downloadsResponse))
//    }.catch { e->
//        emit(Resource.Error(e.message ?: "Unkwown error"))
//    }
//
//    suspend fun getUserDetails(userId: String) : UserDetails?{
//        return userService.getUserDetails(userId)
//    }

}