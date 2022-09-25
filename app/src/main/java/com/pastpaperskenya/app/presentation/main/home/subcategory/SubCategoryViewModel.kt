package com.pastpaperskenya.app.presentation.main.home.subcategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.pastpaperskenya.app.business.model.category.HomeCategory
import com.pastpaperskenya.app.business.model.category.SubCategory
import com.pastpaperskenya.app.business.repository.main.home.SubCategoryRepository
import com.pastpaperskenya.app.business.util.sealed.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    private val subCategoryRepository: SubCategoryRepository
) : ViewModel(){

    private val _id= MutableLiveData<Int>()

    private var _category = _id.switchMap { id->
        subCategoryRepository.getSubCategory(id, 100)
    }

    val category:LiveData<Resource<List<SubCategory>>> = _category

    fun start(id:Int){
        _id.value= id
    }
}