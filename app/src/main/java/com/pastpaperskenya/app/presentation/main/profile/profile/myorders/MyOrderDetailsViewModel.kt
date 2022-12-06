package com.pastpaperskenya.app.presentation.main.profile.profile.myorders

import androidx.lifecycle.*
import com.pastpaperskenya.app.business.model.orders.Orders
import com.pastpaperskenya.app.business.repository.main.profile.MyOrdersDetailRepository
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyOrderDetailsViewModel @Inject constructor(
    private val repository: MyOrdersDetailRepository
): ViewModel() {

    private val _id= MutableLiveData<Int>()

    private var _orders= _id.switchMap { id->
        repository.getMyOrdersDetails(id)
    }

    val response: LiveData<NetworkResult<Orders>> = _orders

    fun start(id: Int){
        _id.value= id
    }
}