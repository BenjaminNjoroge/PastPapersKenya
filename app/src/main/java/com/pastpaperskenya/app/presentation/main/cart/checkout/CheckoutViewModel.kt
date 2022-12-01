package com.pastpaperskenya.app.presentation.main.cart.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.model.mpesa.MpesaPaymentReqResponse
import com.pastpaperskenya.app.business.model.mpesa.MpesaTokenResponse
import com.pastpaperskenya.app.business.model.orders.CreateOrder
import com.pastpaperskenya.app.business.model.user.UserDetails
import com.pastpaperskenya.app.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.app.business.repository.main.cart.CartRepository
import com.pastpaperskenya.app.business.repository.main.payment.PaymentRepository
import com.pastpaperskenya.app.business.repository.main.profile.MyOrdersRepository
import com.pastpaperskenya.app.business.repository.main.profile.ProfileRepository
import com.pastpaperskenya.app.business.util.AuthEvents
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.convertIntoNumeric
import com.pastpaperskenya.app.business.util.sealed.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val profileRepository: ProfileRepository,
    private val datastore: DataStoreRepository,
    private val orderRepository: MyOrdersRepository,
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private var  _cartResponse= MutableLiveData<List<Cart>>()
    val cartResponse: LiveData<List<Cart>> = _cartResponse

    private var _userResponse= MutableLiveData<UserDetails>()
    val userResponse: LiveData<UserDetails> = _userResponse

    private var eventsChannel = Channel<AuthEvents>()
    val events = eventsChannel.receiveAsFlow()

    private var _totalPrice= MutableLiveData<Int?>(0)
    val totalPrice: LiveData<Int?> = _totalPrice

    private var _orderResponse= MutableLiveData<Resource<CreateOrder>>()
    val orderResponse: LiveData<Resource<CreateOrder>> = _orderResponse

    private var _updateorderResponse= MutableLiveData<Resource<CreateOrder>>()
    val updateorderResponse: LiveData<Resource<CreateOrder>> = _updateorderResponse

    private var _mpesaTokenResponse= MutableLiveData<Resource<MpesaTokenResponse>>()
    val mpesaTokenResponse: LiveData<Resource<MpesaTokenResponse>> = _mpesaTokenResponse

    private var _stkpushResponse= MutableLiveData<Resource<MpesaPaymentReqResponse>>()
    val stkpushResponse: LiveData<Resource<MpesaPaymentReqResponse>> = _stkpushResponse


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

    fun getMpesaToken()= viewModelScope.launch{
        _mpesaTokenResponse.value= paymentRepository.getMpesaToken()
    }

     fun createOrder(order: CreateOrder)= viewModelScope.launch{
        _orderResponse.value= orderRepository.createOrder(order)
    }

    fun updateOrder(id: Int, paid: Boolean,  customerId: Int)= viewModelScope.launch{
        _updateorderResponse.value= orderRepository.updateOrder(id, paid, customerId)
    }

    fun createStkpush(total_amount: String, phone_number: String, order_id: String, accesstoken: String)= viewModelScope.launch {
        _stkpushResponse.value= paymentRepository.createStkPush(total_amount, phone_number, order_id, accesstoken)
    }

    private suspend fun getCartItems(){
        cartRepository.getCartItems().collect{
            _cartResponse.postValue(it)
        }
    }

     fun deleteAllCart()= viewModelScope.launch{
        cartRepository.deleteAllCart()
    }

    private suspend fun getUserDetails(userId: Int){
        profileRepository.getUserDetails(userId).collect{
            _userResponse.postValue(it)
        }
    }

    private suspend fun getTotalPrice() {
        cartRepository.getProductCount().collect{
            _totalPrice.postValue(it)
        }
    }
}