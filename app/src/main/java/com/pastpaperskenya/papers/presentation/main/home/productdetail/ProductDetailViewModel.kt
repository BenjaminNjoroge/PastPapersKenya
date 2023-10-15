package com.pastpaperskenya.papers.presentation.main.home.productdetail

import androidx.lifecycle.*
import com.google.firebase.messaging.FirebaseMessaging
import com.pastpaperskenya.papers.business.model.mpesa.MpesaPaymentReqResponse
import com.pastpaperskenya.papers.business.model.mpesa.MpesaTokenResponse
import com.pastpaperskenya.papers.business.model.mpesa.Payment
import com.pastpaperskenya.papers.business.model.orders.CreateOrder
import com.pastpaperskenya.papers.business.model.product.Product
import com.pastpaperskenya.papers.business.model.user.UserDetails
import com.pastpaperskenya.papers.business.model.wishlist.WishList
import com.pastpaperskenya.papers.business.util.AuthEvents
import com.pastpaperskenya.papers.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.papers.business.repository.main.cart.CartRepository
import com.pastpaperskenya.papers.business.repository.main.home.ProductDetailRepository
import com.pastpaperskenya.papers.business.repository.main.payment.PaymentRepository
import com.pastpaperskenya.papers.business.repository.main.profile.MyOrdersRepository
import com.pastpaperskenya.papers.business.repository.main.profile.ProfileRepository
import com.pastpaperskenya.papers.business.repository.main.wishlist.WishlistRepository
import com.pastpaperskenya.papers.business.util.Constants
import com.pastpaperskenya.papers.business.util.convertIntoNumeric
import com.pastpaperskenya.papers.business.util.sealed.NetworkResult
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

    fun savePaymentToFirestore(paymentDetails: Payment)= viewModelScope.launch {
        paymentRepository.savePaymentToFirebase(paymentDetails)

        FirebaseMessaging.getInstance().subscribeToTopic(paymentDetails.checkout_request_id!!)
    }

    fun savePendingPaymentToDatabase(paymentDetails: Payment)= viewModelScope.launch {

        paymentRepository.savePendingPaymentToServer(paymentDetails)
    }
}