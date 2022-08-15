package com.pastpaperskenya.app.business.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.hadiyarajesh.flower.Resource
import kotlinx.coroutines.flow.Flow

fun View.hide(){
    this.visibility= View.INVISIBLE
}

fun View.show(){
    this.visibility= View.VISIBLE
}


fun Fragment.snackbar(message: String)=
    Snackbar.make(this.requireActivity().window.decorView.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()

fun Fragment.toast(res: String){
    Toast.makeText(this.requireContext(), res, Toast.LENGTH_SHORT).show()
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.isConnected(): Boolean {
    var status= false
    val connectionManager= getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        if(connectionManager!= null && connectionManager.activeNetwork!=null && connectionManager.getNetworkCapabilities(connectionManager.activeNetwork) !=null){
            status= true
        }
    } else{
        if (connectionManager.activeNetwork !=null){
            status= true
        }
    }
    return status
}


