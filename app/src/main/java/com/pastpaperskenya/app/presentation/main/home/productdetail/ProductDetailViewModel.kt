package com.pastpaperskenya.app.presentation.main.home.productdetail

import androidx.lifecycle.*
import com.google.firebase.messaging.FirebaseMessaging
import com.pastpaperskenya.app.business.model.mpesa.MpesaPaymentReqResponse
import com.pastpaperskenya.app.business.model.mpesa.MpesaTokenResponse
import com.pastpaperskenya.app.business.model.mpesa.Payment
import com.pastpaperskenya.app.business.model.orders.CreateOrder
import com.pastpaperskenya.app.business.model.product.Product
import com.pastpaperskenya.app.business.model.user.UserDetails
import com.pastpaperskenya.app.business.model.wishlist.WishList
import com.pastpaperskenya.app.business.util.AuthEvents
import com.pastpaperskenya.app.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.app.business.repository.main.cart.CartRepository
import com.pastpaperskenya.app.business.repository.main.home.ProductDetailRepository
import com.pastpaperskenya.app.business.repository.main.payment.PaymentRepository
import com.pastpaperskenya.app.business.repository.main.profile.MyOrdersRepository
import com.pastpaperskenya.app.business.repository.main.profile.ProfileRepository
import com.pastpaperskenya.app.business.repository.main.wishlist.WishlistRepository
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
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductDetailRepository,
    private var profileRepository: ProfileRepository,
    private val datastore: DataStoreRepository,
    private val wishlistRepository: WishlistRepository,
    private val orderRepository: MyOrdersRepository,
    private val paymentRepository: PaymentRepository,
    private val cartRepository: CartRepository,

    ): ViewModel() {

    private var _orderResponse= MutableLiveData<NetworkResult<CreateOrder>>()
    val orderResponse: LiveData<NetworkResult<CreateOrder>> = _orderResponse

    private var _mpesaTokenResponse= MutableLiveData<NetworkResult<MpesaTokenResponse>>()
    val mpesaTokenResponse: LiveData<NetworkResult<MpesaTokenResponse>> = _mpesaTokenResponse

    private var _stkpushResponse= MutableLiveData<NetworkResult<MpesaPaymentReqResponse>>()
    val stkpushResponse: LiveData<NetworkResult<MpesaPaymentReqResponse>> = _stkpushResponse

    private var _userDetails= MutableLiveData<UserDetails>()
    val userDetails: LiveData<UserDetails> = _userDetails

    private var eventsChannel = Channel<AuthEvents>()
    val events = eventsChannel.receiveAsFlow()

     private var _id = MutableLiveData<Int>()

    private var _response= _id.switchMap { id->
        repository.getProductsDetail(id)
    }

    val response: LiveData<NetworkResult<Product>> = _response

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

     fun addToWishlist(wishlist: WishList)= viewModelScope.launch {
         delay(2000)
         wishlistRepository.addItemsToWishlist(wishlist)
         eventsChannel.send(AuthEvents.ErrorCode(100))
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

    fun savePendingPaymentFirestore(paymentDetails: Payment)= viewModelScope.launch {
        paymentRepository.savePendingPaymentFirebase(paymentDetails)

        FirebaseMessaging.getInstance().subscribeToTopic(paymentDetails.CheckoutRequestID.toString())
    }

    fun deleteAllCart()= viewModelScope.launch{
        cartRepository.deleteAllCart()
    }
}