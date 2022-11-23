package com.pastpaperskenya.app.presentation.main.cart.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.business.repository.main.cart.CartRepository
import com.pastpaperskenya.app.business.util.sealed.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.security.interfaces.RSAKey
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: CartRepository): ViewModel() {

    private var _response= MutableLiveData<List<Cart>>()
    val response: LiveData<List<Cart>> get() = _response

    private var eventsChannel = Channel<AuthEvents>()
    val events = eventsChannel.receiveAsFlow()

    private var _totalPrice= MutableLiveData<Int>(0)
    val totalPrice: LiveData<Int> = _totalPrice

    init {
        viewModelScope.launch {
            getCartItems()
        }
    }
    init {
        viewModelScope.launch {
            getTotalPrice()
        }
    }

    private suspend fun getTotalPrice() {
        repository.getProductCount().collect{
            _totalPrice.postValue(it)
        }
    }


    private suspend fun getCartItems(){
        repository.getCartItems().collect{
          _response.postValue(it)
        }
    }

    fun deleteItem(productId: Int)= viewModelScope.launch {
            repository.deleteItems(productId)
        eventsChannel.send(AuthEvents.ErrorCode(100))
    }

}