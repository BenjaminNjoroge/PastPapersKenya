package com.pastpaperskenya.app.presentation.main.cart.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pastpaperskenya.app.business.model.cart.Cart
import com.pastpaperskenya.app.business.model.mpesa.CheckMpesaPaymentStatus
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
import com.pastpaperskenya.app.business.util.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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

    private var _orderResponse= MutableLiveData<NetworkResult<CreateOrder>>()
    val orderResponse: LiveData<NetworkResult<CreateOrder>> = _orderResponse

    private var _mpesaTokenResponse= MutableLiveData<NetworkResult<MpesaTokenResponse>>()
    val mpesaTokenResponse: LiveData<NetworkResult<MpesaTokenResponse>> = _mpesaTokenResponse

    private var _stkpushResponse= MutableLiveData<NetworkResult<MpesaPaymentReqResponse>>()
    val stkpushResponse: LiveData<NetworkResult<MpesaPaymentReqResponse>> = _stkpushResponse

    private var _updateResponse= MutableLiveData<NetworkResult<CreateOrder>>()
    val updateResponse: LiveData<NetworkResult<CreateOrder>> = _updateResponse



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

     fun createOrder(order: CreateOrder)= viewModelScope.launch{
        _orderResponse.value= orderRepository.createOrder(order)
    }

    fun getMpesaToken()= viewModelScope.launch{
        _mpesaTokenResponse.value= paymentRepository.getMpesaToken()
    }

    fun createStkpush(total_amount: String, phone_number: String, order_id: String, accesstoken: String)= viewModelScope.launch {
        _stkpushResponse.value= paymentRepository.createStkPush(total_amount, phone_number, order_id, accesstoken)
    }

    fun checkMpesaPayment(checkoutId: String, accesstoken: String)= viewModelScope.launch {
        try{
            val response= paymentRepository.checkPaymentStatus(checkoutId, accesstoken)
            if (!response.isSuccessful){
                if(response.body()?.checkMpesaPaymentStatusData?.resultCode== "0"){
                    eventsChannel.send(AuthEvents.Message("Payment made successfully"))
                    eventsChannel.send(AuthEvents.ErrorCode(100))
                } else{
                    //delay(1000)
                    eventsChannel.send(AuthEvents.Error("Unable to process payment"))
                }
            } else{
                eventsChannel.send(AuthEvents.Error(response.errorBody().toString()))
            }

        }catch (e: Exception){
            eventsChannel.send(AuthEvents.Error("$e Please check Internet bundles / connection"))
        }
    }

    fun updateOrder(id: Int, paid: Boolean,  customerId: Int)= viewModelScope.launch{
        orderRepository.updateOrder(id, paid, customerId)
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