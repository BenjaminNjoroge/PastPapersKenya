package com.pastpaperskenya.app.business.use_case

import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import kotlinx.coroutines.tasks.await

class Authenticator: BaseAuthenticator {

    private  val TAG = "Authenticator"
    override suspend fun registerUser(email: String, password: String): FirebaseUser? {
        Firebase.auth.createUserWithEmailAndPassword(email, password).await()
        return Firebase.auth.currentUser
    }

    override suspend fun loginUser(email: String, password: String): FirebaseUser? {
        Firebase.auth.signInWithEmailAndPassword(email, password).await()
        return Firebase.auth.currentUser
    }

    override suspend fun sendResetPassword(email: String) {
        Firebase.auth.sendPasswordResetEmail(email).await()
    }

    override fun logout(): FirebaseUser? {
        Firebase.auth.signOut()
        return Firebase.auth.currentUser
    }

    override fun getCurrentUser(): FirebaseUser? {
        return Firebase.auth.currentUser
    }

    override suspend fun signInWithGoogle(credential: AuthCredential): FirebaseUser? {
        Firebase.auth.signInWithCredential(credential).addOnCompleteListener {
            try {
                Log.d(TAG, "signInWithGoogle: "+credential)
            }catch (e: ApiException){
                AuthEvents.Error(e.message.toString())
            }
        }
        return Firebase.auth.currentUser
    }

    override suspend fun signInWithFacebook(credential: AuthCredential): FirebaseUser? {
        Firebase.auth.signInWithCredential(credential)
        return Firebase.auth.currentUser
    }
}