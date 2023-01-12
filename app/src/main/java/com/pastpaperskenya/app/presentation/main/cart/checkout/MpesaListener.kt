package com.pastpaperskenya.app.presentation.main.cart.checkout

interface MpesaListener{
    fun sendingSuccessful(title: String, body: String)
    fun sendingFailed(cause: String)
}