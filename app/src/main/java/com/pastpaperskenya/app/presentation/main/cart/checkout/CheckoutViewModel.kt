package com.pastpaperskenya.app.presentation.main.cart.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.model.user.UserDetails
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.app.business.repository.main.cart.CartRepository
import com.pastpaperskenya.app.business.repository.main.profile.ProfileRepository
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.convertIntoNumeric
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val profileRepository: ProfileRepository,
    private val datastore: DataStoreRepository
) : ViewModel() {

    private var  _cartResponse= MutableLiveData<List<Cart>>()
    val cartResponse: LiveData<List<Cart>> = _cartResponse

    private var _userResponse= MutableLiveData<UserDetails>()
    val userResponse: LiveData<UserDetails> = _userResponse

    private var eventsChannel = Channel<AuthEvents>()
    val events = eventsChannel.receiveAsFlow()

    private var _totalPrice= MutableLiveData<Int?>(0)
    val totalPrice: LiveData<Int?> = _totalPrice

    init {

        viewModelScope.launch {
            try {
                val user = datastore.getValue(Constants.USER_SERVER_ID)
                getUserDetails(convertIntoNumeric(user!!))
            } catch (e: Exception) {
                eventsChannel.send(AuthEvents.Error("Unable to get data $e"))
            }

        }
    }

    init {
        viewModelScope.launch {
            try {
                getCartItems()
            } catch (e: Exception) {
                eventsChannel.send(AuthEvents.Error("Unable to get data $e"))
            }
        }
    }

    init {
        viewModelScope.launch {
            getTotalPrice()
        }
    }

    private suspend fun getCartItems(){
        cartRepository.getCartItems().collect{
            _cartResponse.postValue(it)
        }
    }

    private suspend fun getUserDetails(userId: Int){
        profileRepository.getUserDetails(userId).collect{
            _userResponse.value= it
        }
    }

    private suspend fun getTotalPrice() {
        cartRepository.getProductCount().collect{
            _totalPrice.postValue(it)
        }
    }
}