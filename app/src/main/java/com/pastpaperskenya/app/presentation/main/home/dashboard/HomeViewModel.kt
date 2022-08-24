package com.pastpaperskenya.app.presentation.main.home.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pastpaperskenya.app.business.model.ResourceOne
import com.pastpaperskenya.app.business.model.ApiResponse
import com.pastpaperskenya.app.business.repository.main.PaymentsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val paymentsRepository: PaymentsRepository
) : ViewModel() {

    private val _loading= MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun payMpesa(
        amount: Int,
        mobileNumber: String,
        orderId: String,
        customer:String
    ): LiveData<ResourceOne<ApiResponse>>
    {
        val liveData= MutableLiveData<ResourceOne<ApiResponse>>()

        viewModelScope.launch {
            _loading.value= true
            liveData.value= paymentsRepository.makeLnmoRequest(amount, mobileNumber, orderId, customer)
            _loading.value= false
        }
        return liveData
    }

}