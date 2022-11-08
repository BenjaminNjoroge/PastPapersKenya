package com.pastpaperskenya.app.business.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.util.MutableInt
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.io.IOException
import java.lang.Integer.parseInt

fun convertIntoNumeric(value: String): Int {
    return try {
        parseInt(value)
    }catch (e: NumberFormatException){
        0
    }
}

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




fun getAvailableInternalStorage(): Long {
    val path: File = Environment.getDataDirectory()
    val stat = StatFs(path.path)
    val blockSize: Long
    val availableBlocks: Long

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
        blockSize = stat.blockSizeLong
        availableBlocks = stat.availableBlocksLong
    } else {
        blockSize = stat.blockSize.toLong()
        availableBlocks = stat.availableBlocks.toLong()
    }
    return availableBlocks * blockSize
}

fun Context.isNetworkConnected(): Boolean {
    var isConnected = false
    val connectivityManager= getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    isConnected = true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    isConnected = true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    isConnected = true
                }
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            if (networkInfo != null) {
                if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                    isConnected = true
                } else if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                    isConnected = true
                }
            }
        }
    }
    return isConnected
}