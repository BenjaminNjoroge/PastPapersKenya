package com.pastpaperskenya.app.business.repository.main.home

import com.pastpaperskenya.app.business.services.main.RetrofitService
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SubCategoryRepository @Inject constructor(
    private val categoryService: RetrofitService
) {


}