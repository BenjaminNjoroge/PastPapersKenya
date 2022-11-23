package com.pastpaperskenya.app.presentation.auth.reset

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.business.repository.auth.FirebaseRepository
import com.pastpaperskenya.app.business.repository.datastore.DataStoreRepository
import com.pastpaperskenya.app.business.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val repository: FirebaseRepository,
    private val datastore: DataStoreRepository
) : ViewModel() {

    private val TAG = "ResetPasswordViewModel"

    private var _eventsChannel = Channel<AuthEvents>()
    val resetChannel = _eventsChannel.receiveAsFlow()

    fun resetPassword(email: String) = viewModelScope.launch {
        when {
            email.isEmpty() -> {
                _eventsChannel.send(AuthEvents.ErrorCode(1))
            }
            else -> {
                actualResetPassword(email)
            }
        }
    }

    private fun actualResetPassword(email: String) = viewModelScope.launch {
        try {
            val result = repository.sendResetPassword(email)
            if (result) {
                _eventsChannel.send(AuthEvents.Message("A reset email has been sent to $email. \n Please check your inbox or spam folder of your email"))
                datastore.setValue(Constants.RESET_PASSWORD, Constants.KEY_PASSWORD)
            } else {
                _eventsChannel.send(AuthEvents.Error("Email not sent"))
            }
        } catch (e: Exception) {
            _eventsChannel.send(AuthEvents.Error(e.message.toString()))
        }
    }

}