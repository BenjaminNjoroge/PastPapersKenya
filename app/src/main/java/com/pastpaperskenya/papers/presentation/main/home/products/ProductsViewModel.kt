package com.pastpaperskenya.papers.presentation.main.home.products

import androidx.lifecycle.*
import com.pastpaperskenya.papers.business.model.cart.Cart
import com.pastpaperskenya.papers.business.model.product.Product
import com.pastpaperskenya.papers.business.util.AuthEvents
import com.pastpaperskenya.papers.business.repository.main.home.ProductsRepository
import com.pastpaperskenya.papers.business.util.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductsRepository

): ViewModel() {

    private val eventsChannel = Channel<AuthEvents>()
    val authEventsFlow = eventsChannel.receiveAsFlow()

    private var _categoryId= MutableLiveData<Int>()

    private val _products :LiveData<NetworkResult<List<Product>>> get() = _categoryId.switchMap { id->
        repository.getProducts(100, id.toString()).asLiveData(context = Dispatchers.IO)

    }


    val products: LiveData<NetworkResult<List<Product>>> = _products


    fun start(id: Int){
        _categoryId.value= id
    }

    fun addToCart(cart: Cart){
        viewModelScope.launch {
            repository.insertCartItems(cart)
            eventsChannel.send(AuthEvents.ErrorCode(100))
        }
    }

    fun removeFromCart(productId: Int){
        viewModelScope.launch {
            repository.deleteCartItems(productId)
            eventsChannel.send(AuthEvents.ErrorCode(101))
        }
    }
}