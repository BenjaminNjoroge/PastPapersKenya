package com.pastpaperskenya.app.presentation.main.home.productdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.pastpaperskenya.app.business.model.product.Product
import com.pastpaperskenya.app.business.repository.main.home.ProductDetailRepository
import com.pastpaperskenya.app.business.util.sealed.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductDetailRepository
): ViewModel() {

     private var _id = MutableLiveData<Int>()

    private var _response= _id.switchMap { id->
        repository.getProductsDetail(id)
    }

    val response: LiveData<Resource<Product>> = _response

    fun start(id: Int){
        _id.value= id
    }
}