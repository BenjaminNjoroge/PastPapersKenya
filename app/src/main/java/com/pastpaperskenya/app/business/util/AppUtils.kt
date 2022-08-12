package com.pastpaperskenya.app.business.util

import android.app.Activity
import android.net.ConnectivityManager
import android.os.Build
import android.view.View
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


