package com.pastpaperskenya.app.presentation.main.home.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pastpaperskenya.app.business.model.Category
import com.pastpaperskenya.app.business.repository.main.home.HomeRepository
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private var _categoryResponse= MutableLiveData<NetworkResult<List<Category>>>()
    val category: LiveData<NetworkResult<List<Category>>> = _categoryResponse

    init {
        fetchCategories(0)
    }

    private fun fetchCategories(parent: Int){
        viewModelScope.launch {
            homeRepository.getCategories(parent).collect{
                _categoryResponse.postValue(it)
            }
        }
    }

}