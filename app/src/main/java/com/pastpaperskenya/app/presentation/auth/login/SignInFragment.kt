package com.pastpaperskenya.app.presentation.auth.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.business.util.Result
import com.pastpaperskenya.app.business.util.snackbar
import com.pastpaperskenya.app.databinding.FragmentSignInBinding
import com.pastpaperskenya.app.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val TAG = "SignInFragment"

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

    private val viewModel : SignInViewModel by activityViewModels()
    private var _binding : FragmentSignInBinding? = null
    private val binding get() = _binding
    private lateinit var email: String
    private lateinit var password:String

    companion object{
        private const val RC_SIGN_IN= 9001
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater , container , false)

        userInput()
        registerObservers()
        listenToChannels()

        return binding?.root
    }



    private fun userInput() {

        binding?.apply {
            btnCustomLogin.setOnClickListener {
                email= binding?.inputLoginEmail?.text.toString().trim()
                password= binding?.inputLoginPassword?.text.toString().trim()

                progressBar.isVisible= true
                viewModel.signIn(email, password)
            }
            txtCreateAccount.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            }
            txtForgotPassword.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_resetPasswordFragment)
            }
        }

    }

    private fun listenToChannels(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authEventsFlow.collect{ events->
                when(events){

                    is AuthEvents.Message->{
                        Toast.makeText(requireContext(), events.message, Toast.LENGTH_SHORT).show()
                    }
                    is AuthEvents.Error->{
                        binding?.apply {
                            errorTxt.text= events.message
                            progressBar.isInvisible=true
                        }
                    }
                    is AuthEvents.ErrorCode->{
                            if(events.code== 1)
                                binding?.apply {
                                    inputLoginEmail.error= "Email field should not be empty"
                                    progressBar.isInvisible= true
                                }
                        if (events.code ==2)
                            binding?.apply {
                                inputLoginPassword.error= "Password should not be empty"
                                progressBar.isInvisible= true
                            }
                    }
                }
            }
        }
    }

    private fun registerObservers(){
        viewModel.currentUser.observe(viewLifecycleOwner ,{ user ->
            user?.let {
                launchActivity()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun launchActivity() {
        val intent= Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}