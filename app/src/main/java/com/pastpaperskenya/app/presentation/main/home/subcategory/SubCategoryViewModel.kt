package com.pastpaperskenya.app.presentation.main.home.subcategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pastpaperskenya.app.business.model.category.HomeCategory
import com.pastpaperskenya.app.business.repository.main.home.SubCategoryRepository
import com.pastpaperskenya.app.business.util.sealed.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    private val subCategoryRepository: SubCategoryRepository
) : ViewModel(){

    private val _categories= MutableLiveData<Resource<List<HomeCategory>>>()

    val homeCategory: LiveData<Resource<List<HomeCategory>>> = _categories


}