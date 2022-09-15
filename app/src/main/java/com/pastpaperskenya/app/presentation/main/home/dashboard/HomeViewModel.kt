package com.pastpaperskenya.app.presentation.main.home.dashboard

import androidx.lifecycle.*
import com.pastpaperskenya.app.business.model.Category
import com.pastpaperskenya.app.business.repository.main.home.HomeRepository
import com.pastpaperskenya.app.business.util.sealed.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,

) : ViewModel() {

    var response= homeRepository.getCategory(0, arrayListOf(1032))


}