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
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.pastpaperskenya.app.R
import com.pastpaperskenya.app.business.repository.auth.AuthEvents
import com.pastpaperskenya.app.business.util.Constants
import com.pastpaperskenya.app.business.util.hideKeyboard
import com.pastpaperskenya.app.business.util.toast
import com.pastpaperskenya.app.databinding.FragmentSignInBinding
import com.pastpaperskenya.app.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.util.*


@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val TAG = "SignInFragment"

    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

    private val viewModel : SignInViewModel by activityViewModels()
    private var _binding : FragmentSignInBinding? = null
    private val binding get() = _binding

    private lateinit var email: String
    private lateinit var password:String

    private lateinit var fbemail: String
    private lateinit var fbUsername: String
    private lateinit var fbProfilePhoto: String


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

        auth= FirebaseAuth.getInstance()
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
                LoginManager.getInstance().logInWithReadPermissions(requireActivity(), Arrays.asList("email", "public_profile"))
                LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
                    override fun onCancel() {

                    }

                    override fun onError(error: FacebookException) {
                        toast(error.message.toString())
                    }

                    override fun onSuccess(result: LoginResult) {
                        val idToken= result.accessToken.token
                        toast(idToken)
                        val credential= FacebookAuthProvider.getCredential(idToken)
                        viewModel.signInWithFacebook(credential)

                        val request= GraphRequest.newMeRequest(result.accessToken, object : GraphRequest.GraphJSONObjectCallback{
                            override fun onCompleted(obj: JSONObject?, response: GraphResponse?) {
                                if(obj != null){
                                    try {
                                        fbUsername = obj.getString("name")
                                        fbemail = obj.getString("email")
                                        val fbUserID: String = obj.getString("id")
                                        fbProfilePhoto= obj.getString("https://graph.facebook.com/"+ fbUserID + "/picture?type=large")

                                        disconnectFromFacebook()

                                        // do action after Facebook login success
                                        // or call your API
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    } catch (e: NullPointerException) {
                                        e.printStackTrace()
                                    }
                                }
                            }

                        })

                        val parameters = Bundle()
                        parameters.putString(
                            "fields",
                            "id, name, email, gender, birthday"
                        )
                        request.parameters = parameters
                        request.executeAsync()
                    }

                })
            }
        }
    }

    fun disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return  // already logged out
        }
        GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/me/permissions/",
            null,
            HttpMethod.DELETE,
            object : GraphRequest.Callback {
                override fun onCompleted(graphResponse: GraphResponse) {
                    LoginManager.getInstance().logOut()
                }
            })
            .executeAsync()
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_IN && resultCode== Activity.RESULT_OK){
            val task= GoogleSignIn.getSignedInAccountFromIntent(data)
            val idToken= task.result.idToken

            try {
                val googleSignInAccount= task.getResult(ApiException::class.java)
                if(googleSignInAccount!=null){
                    val credential= GoogleAuthProvider.getCredential(idToken,null)
                    viewModel.signInWithGoogle(credential)
                }
            } catch (e: ApiException){
                Toast.makeText(requireActivity(), e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun launchActivity() {
        val intent= Intent(context, MainActivity::class.java)
        intent.putExtra(Constants.KEY_EMAIL, fbemail)
        intent.putExtra(Constants.KEY_USERNAME, fbUsername)
        intent.putExtra(Constants.KEY_PROFILE_PHOTO, fbProfilePhoto)
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