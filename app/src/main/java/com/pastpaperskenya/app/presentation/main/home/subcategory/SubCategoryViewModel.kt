package com.pastpaperskenya.app.presentation.main.home.subcategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pastpaperskenya.app.business.model.Category
import com.pastpaperskenya.app.business.repository.main.home.SubCategoryRepository
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    private val subCategoryRepository: SubCategoryRepository
) : ViewModel(){

    private val _categories= MutableLiveData<NetworkResult<List<Category>>>()

    val category: LiveData<NetworkResult<List<Category>>> = _categories


}