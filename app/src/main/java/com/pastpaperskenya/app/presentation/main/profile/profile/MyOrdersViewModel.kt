package com.pastpaperskenya.app.presentation.main.profile.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pastpaperskenya.app.business.model.auth.UserDetails
import com.pastpaperskenya.app.business.model.orders.Orders
import com.pastpaperskenya.app.business.repository.main.profile.MyOrdersRepository
import com.pastpaperskenya.app.business.util.sealed.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyOrdersViewModel @Inject constructor(private val repository: MyOrdersRepository) : ViewModel() {

    private var _response = MutableLiveData<Resource<List<Orders>>>()
    val response: LiveData<Resource<List<Orders>>> = _response

    init {
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser?.uid

        viewModelScope.launch {
            val id = user?.let { repository.getUserDetails(it) }
            fetchOrders(id?.userServerId!!.toInt())
        }
    }

    private fun fetchOrders(customerId: Int) = viewModelScope.launch {
        repository.getMyOrders(customerId).collect {
            _response.postValue(it)
        }
    }
}