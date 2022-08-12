package com.pastpaperskenya.app.business.repository.auth

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.pastpaperskenya.app.business.util.SingleLiveEvent

class LiveMessageEvent<T> : SingleLiveEvent<(T.() -> Unit)?>() {

    fun setEventReceiver(owner: LifecycleOwner, receiver: T) {
        observe(owner, Observer { event ->
            if ( event != null ) {
                receiver.event()
            }
        })
    }

    fun sendEvent(event: (T.() -> Unit)?) {
        value = event
    }
}