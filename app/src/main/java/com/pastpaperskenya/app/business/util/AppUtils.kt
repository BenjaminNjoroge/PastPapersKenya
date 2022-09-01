package com.pastpaperskenya.app.business.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow

fun sanitizePhoneNumber(phone: String): String{
    return when{
        phone.startsWith("0") -> phone.replaceFirst("^0".toRegex(), "254")
        phone.startsWith("+") -> phone.replaceFirst("^\\+".toRegex(), "")
        else -> phone
    }
}

fun View.hide(){
    this.visibility= View.INVISIBLE
}

fun View.show(){
    this.visibility= View.VISIBLE
}

fun Context.createDialog(layout: Int, cancelable: Boolean): Dialog {
    val dialog = Dialog(this, android.R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(layout)
    dialog.window?.setGravity(Gravity.CENTER)
    dialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(cancelable)
    return dialog
}

fun Fragment.snackbar(message: String)=
    Snackbar.make(this.requireActivity().window.decorView.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()

fun Fragment.toast(res: String){
    Toast.makeText(this.requireActivity(), res, Toast.LENGTH_SHORT).show()
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

@RequiresApi(Build.VERSION_CODES.M)
fun Activity.isConnected(): Boolean {
    var status= false
    val connectionManager= getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        if(connectionManager?.activeNetwork != null && connectionManager.getNetworkCapabilities(connectionManager.activeNetwork) !=null){
            status= true
        }
    } else{
        if (connectionManager.activeNetwork !=null){
            status= true
        }
    }
    return status
}


