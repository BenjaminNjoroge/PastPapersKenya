package com.pastpaperskenya.app.presentation.main.cart.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.repository.main.cart.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: CartRepository): ViewModel() {

    private var _response= MutableLiveData<List<Cart>>()
    val response: LiveData<List<Cart>> get() = _response

    init {
        viewModelScope.launch {
            repository.getCartItems().collect{ items->
                _response.postValue(items)
            }
        }
    }

    fun deleteItem(cart: Cart){
        viewModelScope.launch {
            repository.deleteItems(cart)
        }
    }

}