package com.pastpaperskenya.app.business.model

import android.os.Parcelable
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetails(
    val userId:String,
    val email: String?,
    val phone: String?,
    val firstname: String?,
    val lastname: String?,
    val country: String?,
    val county: String?
) : Parcelable{

    companion object{
        fun DocumentSnapshot.toUserDetails(): UserDetails?{
            try {
                val userId= getString("userId")
                val email= getString("email")
                val phone= getString("phone")
                val firstname= getString("firstname")
                val lastname= getString("lastname")
                val country= getString("country")
                val county= getString("county")
                return UserDetails(userId!!, email, phone, firstname, lastname, country, county)

            } catch (e: Exception){
                Log.e(TAG, "Error converting user profile", e)
                FirebaseCrashlytics.getInstance().log("Error converting user profile")
                FirebaseCrashlytics.getInstance().setCustomKey("userId", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                return null
            }
           }
        private const val TAG = "UserDetails"
    }
}
