package com.pastpaperskenya.app.presentation.auth.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.facebook.*
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.model.user.UserDetails
import com.pastpaperskenya.app.business.util.AuthEvents
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.hideKeyboard
import com.pastpaperskenya.app.business.util.network.NetworkChangeReceiver
import com.pastpaperskenya.app.business.util.toast
import com.pastpaperskenya.app.databinding.FragmentSignInBinding
import com.pastpaperskenya.app.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class SignInFragment : Fragment() {

    private val TAG = "SignInFragment"

    private val viewModel: SignInViewModel by activityViewModels()
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding

    private lateinit var email: String
    private lateinit var password: String

    private lateinit var fbemail: String
    private lateinit var fbUsername: String
    private lateinit var fbProfilePhoto: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(requireActivity());
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)

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

            }

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
    }

    private fun registerObservers() {

        viewModel.userResponse.observe(viewLifecycleOwner) { response ->

            if (response.isSuccessful) {

                var userServerId: Int=0

                for (user in response.body()!!){
                    userServerId = user.id!!
                }

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