package com.pastpaperskenya.app.presentation.auth.login

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
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.business.util.hideKeyboard
import com.pastpaperskenya.app.business.util.toast
import com.pastpaperskenya.app.databinding.FragmentSignInBinding
import com.pastpaperskenya.app.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        private const val RC_FACEBOOK_IN= 9001
        private const val RC_GOOGLE_IN= 9002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(requireActivity());
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater , container , false)

        initGoogleSignInClient()
        initFacebookLogin()
        userInput()
        registerObservers()
        listenToChannels()

        return binding?.root
    }

    private fun initGoogleSignInClient(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("346798231053-s0n8nr0r5vq9e26i96q5herhoum3idjv.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient= GoogleSignIn.getClient(requireActivity(), gso)

    }

    private fun initFacebookLogin(){
        callbackManager= CallbackManager.Factory.create()
    }

    private fun userInput() {

        binding?.apply {
            btnCustomLogin.setOnClickListener {
                email= binding?.inputLoginEmail?.text.toString().trim()
                password= binding?.inputLoginPassword?.text.toString().trim()

                hideKeyboard()
                progressBar.isVisible= true
                viewModel.signIn(email, password)
            }
            txtCreateAccount.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            }
            txtForgotPassword.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_resetPasswordFragment)
            }

            gpLoginBtn.setOnClickListener{
                val intent= googleSignInClient.signInIntent
                startActivityForResult(intent, RC_GOOGLE_IN)
            }

            fbLoginBtn.setOnClickListener {
                fbLoginBtn.setReadPermissions("email", "public_profile")
                fbLoginBtn.registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
                    override fun onCancel() {

                    }

                    override fun onError(error: FacebookException) {
                        TODO("Not yet implemented")
                    }

                    override fun onSuccess(result: LoginResult) {
                        viewModel.firebaseSignInWithFacebook(result.accessToken.token)
                    }

                })
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
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                launchActivity()
            }
        }
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "super.onActivityResult(requestCode, resultCode, data)",
        "androidx.fragment.app.Fragment"
    )
    )
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == RC_GOOGLE_IN){
             val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val googleSignInAccount= task.getResult(ApiException::class.java)
                if(googleSignInAccount!=null){
                    Toast.makeText(requireContext(), googleSignInAccount.idToken, Toast.LENGTH_SHORT).show()
                    viewModel.firebaseSignInWithGoogle(googleSignInAccount.idToken!!)
                }
            } catch (e: ApiException){
                Toast.makeText(requireActivity(), e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
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