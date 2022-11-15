package com.pastpaperskenya.app.presentation.main.home.products

import androidx.lifecycle.*
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.model.download.Download
import com.pastpaperskenya.app.business.model.product.Product
import com.pastpaperskenya.app.business.model.product.ProductCategory
import com.pastpaperskenya.app.business.repository.main.home.ProductsRepository
import com.pastpaperskenya.app.business.util.sealed.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductsRepository

): ViewModel() {

    private var _categoryId= MutableLiveData<Int>()

    private val _products :LiveData<Resource<List<Product>>> get() = _categoryId.switchMap { id->
        repository.getProducts(100, id.toString()).asLiveData(context = Dispatchers.IO)

    }


    val products: LiveData<Resource<List<Product>>> = _products


    fun start(id: Int){
        _categoryId.value= id
    }

    fun addToCart(cart: Cart){
        viewModelScope.launch {
            repository.insertCartItems(cart)
        }
    }

    fun removeFromCart(productId: Int){
        viewModelScope.launch {
            repository.deleteCartItems(productId)
        }
    }
}