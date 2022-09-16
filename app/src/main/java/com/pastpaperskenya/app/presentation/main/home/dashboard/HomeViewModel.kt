package com.pastpaperskenya.app.presentation.main.home.dashboard

import androidx.lifecycle.*
import com.pastpaperskenya.app.business.repository.main.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,

) : ViewModel() {

    var homeResponse= homeRepository.getParentCategory(0, arrayListOf(1032))
    var sliderResponse= homeRepository.getSliderCategory(1032)


}