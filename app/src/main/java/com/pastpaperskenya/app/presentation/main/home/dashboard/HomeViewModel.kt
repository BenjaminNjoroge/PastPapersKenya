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
    private val homeRepository: HomeRepository,

) : ViewModel() {

    private var _categoryResponse= MutableLiveData<NetworkResult<List<Category>>>()
    val category: LiveData<NetworkResult<List<Category>>> = _categoryResponse

    private var _sliderImages= MutableLiveData<NetworkResult<List<Category>>>()
    val sliderImages: LiveData<NetworkResult<List<Category>>> = _sliderImages


    init {
        fetchCategories(0, arrayListOf(1032))
        sliderImages(1032)
    }

    private fun fetchCategories(parent: Int, slider: ArrayList<Int>){
        viewModelScope.launch {
            homeRepository.getCategories(parent, slider).collect{
                _categoryResponse.postValue(it)

            }
        }
    }

    private fun sliderImages(parent:Int){
        viewModelScope.launch {
            homeRepository.getSliderCategories(parent).collect{
                _sliderImages.postValue(it)
            }
        }
    }

}