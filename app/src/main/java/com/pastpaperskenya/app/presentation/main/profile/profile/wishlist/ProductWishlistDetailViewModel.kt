package com.pastpaperskenya.app.presentation.main.profile.profile.wishlist

import androidx.lifecycle.*
import com.pastpaperskenya.app.business.model.product.Product
import com.pastpaperskenya.app.business.model.user.UserDetails
import com.pastpaperskenya.app.business.util.AuthEvents
import com.pastpaperskenya.app.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.app.business.repository.main.home.ProductDetailRepository
import com.pastpaperskenya.app.business.repository.main.profile.ProfileRepository
import com.pastpaperskenya.app.business.repository.main.wishlist.WishlistRepository
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.convertIntoNumeric
import com.pastpaperskenya.app.business.util.sealed.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductWishlistDetailViewModel @Inject constructor(
    private val repository: ProductDetailRepository,
    private var profileRepository: ProfileRepository,
    private val datastore: DataStoreRepository,
    private val wishlistRepository: WishlistRepository
): ViewModel() {

    private var _userDetails= MutableLiveData<UserDetails>()
    val userDetails: LiveData<UserDetails> = _userDetails

    private var eventsChannel = Channel<AuthEvents>()
    val events = eventsChannel.receiveAsFlow()

     private var _id = MutableLiveData<Int>()

    private var _response= _id.switchMap { id->
        repository.getProductsDetail(id)
    }

    val response: LiveData<Resource<Product>> = _response

    fun start(id: Int){
        _id.value= id
    }

    init {
        viewModelScope.launch {
            val id = datastore.getValue(Constants.USER_SERVER_ID)
            try {
                getUserDetails(convertIntoNumeric(id!!))
            } catch (e: Exception) {
                eventsChannel.send(AuthEvents.Error("Unable to get data $e"))
            }
        }

    }

    private suspend fun getUserDetails(userId: Int){
        profileRepository.getUserDetails(userId).collect{
            _userDetails.postValue(it)
        }
    }


}