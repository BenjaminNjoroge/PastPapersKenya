package com.pastpaperskenya.app.business.model.auth

import android.os.Parcelable
import android.util.Log
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Entity(tableName = "users")
data class UserDetails(
    val userId:String?=null,
    val email: String?= null,
    val phone: String?=null,
    val firstname: String?=null,
    val lastname: String?=null,
    val country: String?=null,
    val county: String?=null,
    @PrimaryKey(autoGenerate = false)
    val userServerId: Int?=null
)

//    companion object{
//        fun DocumentSnapshot.toUserDetails(): UserDetails?{
//            try {
//                val userId= getString("userId")
//                val email= getString("email")
//                val phone= getString("phone")
//                val firstname= getString("firstname")
//                val lastname= getString("lastname")
//                val country= getString("country")
//                val county= getString("county")
//                val userServerId= getLong("userServerId")
//                return UserDetails(userId!!, email, phone, firstname, lastname, country, county,
//                    userServerId!!
//                )
//
//            } catch (e: Exception){
//                Log.e(TAG, "Error converting user profile", e)
//                FirebaseCrashlytics.getInstance().log("Error converting user profile")
//                FirebaseCrashlytics.getInstance().setCustomKey("userId", id)
//                FirebaseCrashlytics.getInstance().recordException(e)
//                return null
//            }
//           }
//        private const val TAG = "UserDetails"
//    }
//}
