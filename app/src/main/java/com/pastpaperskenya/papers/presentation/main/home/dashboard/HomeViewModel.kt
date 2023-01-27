package com.pastpaperskenya.papers.presentation.main.home.dashboard

import androidx.lifecycle.*
import com.pastpaperskenya.papers.business.repository.main.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,

) : ViewModel() {

    var homeResponse= homeRepository.getParentCategory(0, arrayListOf(1032))
    var sliderResponse= homeRepository.getSliderCategory(1032)


}