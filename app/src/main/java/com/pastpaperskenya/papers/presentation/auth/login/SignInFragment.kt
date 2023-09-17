package com.pastpaperskenya.papers.presentation.auth.login

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.facebook.FacebookSdk
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.pastpaperskenya.papers.BuildConfig
import com.pastpaperskenya.papers.business.model.user.UserDetails
import com.pastpaperskenya.papers.business.util.AuthEvents
import com.pastpaperskenya.papers.business.util.Constants
import com.pastpaperskenya.papers.business.util.hideKeyboard
import com.pastpaperskenya.papers.business.util.network.NetworkChangeReceiver
import com.pastpaperskenya.papers.databinding.FragmentSignInBinding
import com.pastpaperskenya.papers.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import com.pastpaperskenya.papers.R
import com.pastpaperskenya.papers.presentation.BaseApplication.app
import io.realm.mongodb.Credentials
import io.realm.mongodb.auth.GoogleAuthType
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class SignInFragment : Fragment() {

    private val TAG = "SignInFragment"

    private val viewModel: SignInViewModel by activityViewModels()
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding

    private lateinit var email: String
    private lateinit var password: String

    private var oneTapClient: SignInClient? = null
    private var signUpRequest: BeginSignInRequest? = null
    private var signInRequest: BeginSignInRequest? = null

    private val oneTapResult = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){ result ->
        try {
            val credential = oneTapClient?.getSignInCredentialFromIntent(result.data)
            val idToken = credential?.googleIdToken
            when {
                idToken != null -> {
                    // Got an ID token from Google. Use it to authenticate
                    // with your backend.
                    val msg = "idToken: $idToken"
                    //Snackbar.make(binding!!.root, msg, Snackbar.LENGTH_SHORT).show()
                    Log.d(TAG,"one tap ${msg}")
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    viewModel.actualGoogleSignIn(firebaseCredential)

                    runBlocking {
                        app.loginAsync(Credentials.google(idToken, GoogleAuthType.ID_TOKEN)){ user->
                            if(user.isSuccess){
                                Toast.makeText(requireContext(), "Successfully authenticated using Google OAuth", Toast.LENGTH_SHORT).show()
                                Log.d("MainActivity", "Successfully authenticated using Google OAuth")
                                launchActivity()
                            } else{
                                Log.d("MainActivity", "Failed to Log in to MongoDB Realm: ${user.error.errorMessage}")
                                Snackbar.make(binding!!.root, user.error.errorMessage.toString(), Snackbar.LENGTH_INDEFINITE).show()
                            }
                        }
                    }

                }
                else -> {
                    // Shouldn't happen.
                    Log.d(TAG, "No ID token!")
                    Snackbar.make(binding!!.root, "No ID token!", Snackbar.LENGTH_INDEFINITE).show()
                }
            }
        } catch (e: ApiException) {
            when (e.statusCode) {
                CommonStatusCodes.CANCELED -> {
                    Log.d(TAG, "One-tap dialog was closed.")
                    // Don't re-prompt the user.
                    Snackbar.make(binding!!.root, "One-tap dialog was closed.", Snackbar.LENGTH_INDEFINITE).show()
                }
                CommonStatusCodes.NETWORK_ERROR -> {
                    Log.d(TAG, "One-tap encountered a network error.")
                    // Try again or just ignore.
                    Snackbar.make(binding!!.root, "One-tap encountered a network error.", Snackbar.LENGTH_INDEFINITE).show()
                }
                else -> {
                    Log.d(TAG, "Couldn't get credential from result." +
                            " (${e.localizedMessage})")
                    Snackbar.make(binding!!.root, "Couldn't get credential from result.\" +\n" +
                            " (${e.localizedMessage})", Snackbar.LENGTH_INDEFINITE).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)

        oneTapClient = Identity.getSignInClient(requireActivity())
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .build()

        userInput()
        registerObservers()
        listenToChannels()

        return binding?.root
    }


    private fun userInput() {

        binding?.apply {
            btnCustomLogin.setOnClickListener {
                email = binding?.inputLoginEmail?.text.toString().trim()
                password = binding?.inputLoginPassword?.text.toString().trim()

                hideKeyboard()
                progressBar.isVisible = true

                if (NetworkChangeReceiver.isNetworkConnected()) {
                    viewModel.fieldsChecker(email, password)
                } else {
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, "Please check internet connection", Toast.LENGTH_SHORT)
                        .show()
                }

            }
            txtCreateAccount.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            }

            txtForgotPassword.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_resetPasswordFragment)
            }

            gpLoginBtn.setOnClickListener {
                binding?.progressBar?.visibility= View.VISIBLE
                displaySignIn()
            }

        }
    }

    private fun displaySignIn(){
        oneTapClient?.beginSignIn(signInRequest!!)
            ?.addOnSuccessListener(requireActivity()) { result ->
                try {
                    binding?.progressBar?.visibility= View.GONE
                    val ib = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    oneTapResult.launch(ib)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("btn click", "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            ?.addOnFailureListener(requireActivity()) { e ->
                // No Google Accounts found. Just continue presenting the signed-out UI.
                displaySignUp()
                Log.d("btn click", e.localizedMessage!!)
            }
    }

    private fun displaySignUp() {
        oneTapClient?.beginSignIn(signUpRequest!!)
            ?.addOnSuccessListener(requireActivity()) { result ->
                try {
                    binding?.progressBar?.visibility= View.GONE
                    val ib = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    oneTapResult.launch(ib)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            ?.addOnFailureListener(requireActivity()) { e ->
                // No Google Accounts found. Just continue presenting the signed-out UI.
                displaySignUp()
                Log.d("btn click", e.localizedMessage!!)
            }
    }

    private fun listenToChannels() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authEventsFlow.collect { events ->
                when (events) {

                    is AuthEvents.Message -> {
                        Toast.makeText(requireContext(), events.message, Toast.LENGTH_SHORT).show()
                    }
                    is AuthEvents.Error -> {
                        binding?.apply {
                            errorTxt.text = events.message
                            progressBar.isInvisible = true
                        }
                        Toast.makeText(requireContext(), events.message, Toast.LENGTH_SHORT).show()
                    }
                    is AuthEvents.ErrorCode -> {
                        if (events.code == 1)
                            binding?.apply {
                                inputLoginEmail.error = "Email field should not be empty"
                                progressBar.isInvisible = true
                            }
                        if (events.code == 2)
                            binding?.apply {
                                inputLoginPassword.error = "Password should not be empty"
                                progressBar.isInvisible = true
                            }
                    }
                }
            }
        }
        viewModel.localResponse.observe(viewLifecycleOwner){
            Toast.makeText(context, "User data saved to database", Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerObservers() {

        viewModel.userResponse.observe(viewLifecycleOwner) { response ->
            var userServerId =0
            var emailad = ""
            var phone = ""
            var firstname =""
            var lastname = ""
            var county =""
            var country =""
            var profileImage=""

            if (response.isSuccessful) {

                response.body()?.forEach {
                    userServerId= it.id!!
                    emailad= it.email.toString()
                    phone= it.billingAddress?.phone.toString()
                    firstname= it.firstname.toString()
                    lastname= it.lastname.toString()
                    county= it.billingAddress?.state.toString()
                    country= it.billingAddress?.country.toString()
                    profileImage= it.image.toString()
                }

                val localuser= UserDetails("", emailad, phone, firstname, lastname, country, county, userServerId, profileImage)

                viewModel.writeToDataStore(Constants.USER_SERVER_ID, userServerId.toString())

                viewModel.insertUserDetails(localuser)

                viewModel.actualSignInUser(email, password, userServerId)
            } else {
                Toast.makeText(
                    requireContext(),
                    response.errorBody().toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                launchActivity()
            }
        }
    }

    private fun launchActivity() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}