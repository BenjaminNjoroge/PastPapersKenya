package com.pastpaperskenya.app.presentation.main.profile.profile.myorders

import androidx.lifecycle.*
import com.pastpaperskenya.app.business.model.orders.Orders
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.app.business.repository.main.profile.MyOrdersRepository
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.convertIntoNumeric
import com.pastpaperskenya.app.business.util.sealed.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MyOrdersViewModel @Inject constructor(
    private val repository: MyOrdersRepository,
    private val datastore: DataStoreRepository
) : ViewModel() {

    private var eventsChannel= Channel<AuthEvents>()
    val events= eventsChannel.receiveAsFlow()

    private var _id= MutableLiveData<Int>()


    private var _category=
        _id.switchMap {
            repository.getMyOrders(it)
        }


    init {
        viewModelScope.launch {
            try {
                _id.value = convertIntoNumeric(getUserId()!!)
            } catch (e:Exception){
                eventsChannel.send(AuthEvents.Error("Unable to get data $e"))
            }
        }
    }

    val response:LiveData<Resource<List<Orders>>> = _category

    private suspend fun getUserId():String?{
        return datastore.getValue(Constants.USER_SERVER_ID)
    }
}