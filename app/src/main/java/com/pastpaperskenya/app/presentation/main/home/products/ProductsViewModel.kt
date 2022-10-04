package com.pastpaperskenya.app.presentation.main.home.products

import androidx.lifecycle.*
import com.pastpaperskenya.app.business.model.product.Product
import com.pastpaperskenya.app.business.repository.main.home.ProductsRepository
import com.pastpaperskenya.app.business.util.sealed.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductsRepository

): ViewModel() {

    private var _id= MutableLiveData<Int>()

    private var _products= _id.switchMap { id->
        repository.getProducts(100, id)
    }
    val products: LiveData<Resource<List<Product>>> = _products


    fun start(id: Int){
        _id.value= id
    }
}