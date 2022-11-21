package com.pastpaperskenya.app.presentation.main.profile.profile.wishlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.model.wishlist.WishList
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.business.repository.main.wishlist.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishListViewModel @Inject constructor(
    private val wishlistRepository: WishlistRepository): ViewModel() {


    private var _response= MutableLiveData<List<WishList>>()
    val response: LiveData<List<WishList>> get() = _response

    private var eventsChannel = Channel<AuthEvents>()
    val events = eventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            getWishlistItems()
        }
    }


    private suspend fun getWishlistItems(){
        wishlistRepository.getWishlistItems().collect{
            _response.postValue(it)
        }
    }

    fun deleteItem(productId: Int)= viewModelScope.launch {
        wishlistRepository.deleteItems(productId)
        eventsChannel.send(AuthEvents.ErrorCode(100))
    }
}